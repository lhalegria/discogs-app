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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.main.viewmodel.MainState
import kotlinx.coroutines.flow.flowOf
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
    artists: LazyPagingItems<ArtistSummaryModel>,
    onQueryChanged: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onArtistSelected: (ArtistSummaryModel) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChanged,
            label = { Text(text = "Search artist") },
            singleLine = true,
            leadingIcon = { Text(text = "🔎") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchSubmitted() }),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
        )

        val contentState =
            when {
                !state.hasSearched -> ContentState.EmptyBeforeSearch
                artists.loadState.refresh is LoadState.Loading -> ContentState.Loading
                artists.loadState.refresh is LoadState.Error -> ContentState.Error
                artists.itemCount == 0 -> ContentState.EmptyResults
                else -> ContentState.Results
            }

        AnimatedContent(
            targetState = contentState,
            label = "main-content-state",
            modifier = Modifier.fillMaxSize(),
        ) { targetState ->
            when (targetState) {
                ContentState.Loading -> {
                    MainContentLoading()
                }

                ContentState.Error -> {
                    MainContentError(artists.loadState.refresh as? LoadState.Error)
                }

                ContentState.EmptyBeforeSearch -> {
                    EmptyStateMessage(message = "Search for an artist to get started.")
                }

                ContentState.EmptyResults -> {
                    EmptyStateMessage(message = "No artists found for your query.")
                }

                ContentState.Results -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = artists.itemCount,
                            key = artists.itemKey { it.id },
                        ) { index ->
                            val artist = artists[index] ?: return@items
                            ArtistRow(
                                artist = artist,
                                onClick = { onArtistSelected(artist) },
                            )
                        }

                        if (artists.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContentLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MainContentError(error: LoadState.Error?) {
    Text(
        text = error?.error?.message ?: "Failed to fetch artists. Please try again.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
private fun EmptyStateMessage(message: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
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
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = onClick)
                .padding(12.dp),
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

    if (url.isBlank()) {
        ArtistThumbnailPlaceholder(name = artist.title)
        return
    }

    SubcomposeAsyncImage(
        model = url,
        contentDescription = artist.title,
        contentScale = ContentScale.Crop,
        loading = { ArtistThumbnailPlaceholder(name = artist.title) },
        error = { ArtistThumbnailPlaceholder(name = artist.title) },
        modifier =
            Modifier
                .size(52.dp)
                .clip(CircleShape),
    )
}

@Composable
private fun ArtistThumbnailPlaceholder(name: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
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
            artists = flowOf(PagingData.empty<ArtistSummaryModel>()).collectAsLazyPagingItems(),
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
            state =
                MainState(
                    query = "Radiohead",
                    hasSearched = true,
                ),
            artists =
                flowOf(
                    PagingData.from(
                        listOf(
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
                ).collectAsLazyPagingItems(),
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
            artist =
                ArtistSummaryModel(
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
