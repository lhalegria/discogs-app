package com.example.discogsapp.main.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.main.viewmodel.MainState
import java.util.Locale

private enum class ContentState {
    Loading,
    Error,
    EmptyBeforeSearch,
    EmptyResults,
    Results,
}

@Composable
fun MainContent(
    state: MainState,
    onQueryChanged: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onArtistSelected: (ArtistSummaryModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            value = state.query,
            onValueChange = onQueryChanged,
            label = { Text(text = "Search artist") },
            singleLine = true,
            leadingIcon = { Text(text = "🔎") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchSubmitted() }),
        )

        val contentState = when {
            state.isLoading -> ContentState.Loading
            state.errorMessage != null -> ContentState.Error
            !state.hasSearched -> ContentState.EmptyBeforeSearch
            state.artists.isEmpty() -> ContentState.EmptyResults
            else -> ContentState.Results
        }

        AnimatedContent(
            targetState = contentState,
            modifier = Modifier.fillMaxSize(),
            label = "main-content-state",
        ) { targetState ->
            when (targetState) {
                ContentState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                ContentState.Error -> {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = state.errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                ContentState.EmptyBeforeSearch -> {
                    EmptyStateMessage(message = "Search for an artist to get started.")
                }

                ContentState.EmptyResults -> {
                    EmptyStateMessage(message = "No artists found for your query.")
                }

                ContentState.Results -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = state.artists,
                            key = { it.id },
                        ) { artist ->
                            ArtistRow(
                                artist = artist,
                                onClick = { onArtistSelected(artist) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ArtistRow(
    artist: ArtistSummaryModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArtistThumbnail(artist = artist)

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = artist.title,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun ArtistThumbnail(artist: ArtistSummaryModel) {
    val url = artist.thumbnailUrl

    if (url.isNullOrBlank()) {
        ArtistThumbnailPlaceholder(name = artist.title)
        return
    }

    SubcomposeAsyncImage(
        model = url,
        contentDescription = artist.title,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        loading = { ArtistThumbnailPlaceholder(name = artist.title) },
        error = { ArtistThumbnailPlaceholder(name = artist.title) },
    )
}

@Composable
private fun ArtistThumbnailPlaceholder(name: String) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.take(1).uppercase(Locale.getDefault()).ifBlank { "?" },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MainContentInitialPreview() {
    MaterialTheme {
        MainContent(
            state = MainState(),
            onQueryChanged = {},
            onSearchSubmitted = {},
            onArtistSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainContentResultsPreview() {
    MaterialTheme {
        MainContent(
            state = MainState(
                query = "Radiohead",
                hasSearched = true,
                artists = listOf(
                    ArtistSummaryModel(
                        id = 1,
                        title = "Radiohead",
                        thumbnailUrl = "",
                        type = "artist",
                    ),
                    ArtistSummaryModel(
                        id = 2,
                        title = "Thom Yorke",
                        thumbnailUrl = "",
                        type = "artist",
                    ),
                ),
            ),
            onQueryChanged = {},
            onSearchSubmitted = {},
            onArtistSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistRowPreview() {
    MaterialTheme {
        ArtistRow(
            artist = ArtistSummaryModel(
                id = 1,
                title = "Massive Attack",
                thumbnailUrl = "",
                type = "artist",
            ),
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistThumbnailPlaceholderPreview() {
    MaterialTheme {
        ArtistThumbnailPlaceholder(name = "Portishead")
    }
}
