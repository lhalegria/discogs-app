package com.example.discogsapp.album.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.discogsapp.R
import com.example.discogsapp.album.viewmodel.ReleaseState
import com.example.discogsapp.common.compose.EmptyState
import com.example.discogsapp.common.compose.ErrorState
import com.example.discogsapp.common.compose.LoadingState
import com.example.discogsapp.common.compose.TopBar
import com.example.discogsapp.domain.model.ArtistReleaseModel
import kotlinx.coroutines.flow.flowOf

private const val YEAR_SIZE = 4

private enum class ContentState {
    Loading,
    Error,
    EmptyResults,
    Results,
}

@Composable
fun ReleaseContent(
    state: ReleaseState,
    releases: LazyPagingItems<ArtistReleaseModel>,
    navigateBack: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onYearFilterSelected: () -> Unit,
    onGenreFilterSelected: () -> Unit,
    onLabelFilterSelected: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) {
        TopBar(
            title = stringResource(R.string.releases_title),
            onNavigateBack = navigateBack,
        )

        Search(
            state = state,
            onQueryChanged = onQueryChanged,
            onSearchSubmitted = onSearchSubmitted,
            onYearFilterSelected = onYearFilterSelected,
            onGenreFilterSelected = onGenreFilterSelected,
            onLabelFilterSelected = onLabelFilterSelected,
        )

        val contentState =
            when {
                releases.loadState.refresh is LoadState.Loading -> ContentState.Loading
                releases.loadState.refresh is LoadState.Error -> ContentState.Error
                releases.itemCount == 0 -> ContentState.EmptyResults
                else -> ContentState.Results
            }

        AnimatedContent(
            targetState = contentState,
            label = "main-content-state",
            modifier = Modifier.fillMaxSize(),
        ) { targetState ->
            when (targetState) {
                ContentState.Loading -> {
                    LoadingState(message = stringResource(R.string.loading_releases))
                }

                ContentState.Error -> {
                    ErrorState(
                        errorMessage = stringResource(R.string.error_loading_releases),
                        isButtonEnabled = false,
                        onRetry = onSearchSubmitted,
                        onGoBack = navigateBack,
                    )
                }

                ContentState.EmptyResults -> {
                    EmptyState(message = stringResource(R.string.no_results))
                }

                ContentState.Results -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = releases.itemCount,
                            key = { index ->
                                val item = releases[index]
                                item?.let { "${it.type}:${it.id}" } ?: "placeholder_$index"
                            },
                        ) { index ->
                            val release = releases[index] ?: return@items
                            ReleaseCard(release = release)
                        }

                        if (releases.loadState.append is LoadState.Loading) {
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
private fun Search(
    state: ReleaseState,
    onQueryChanged: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onYearFilterSelected: () -> Unit,
    onGenreFilterSelected: () -> Unit,
    onLabelFilterSelected: () -> Unit,
) {
    OutlinedTextField(
        value = state.query,
        onValueChange = { input ->
            if (state.isYearFilter) {
                val filtered =
                    input
                        .filter { it.isDigit() }
                        .take(YEAR_SIZE)

                onQueryChanged(filtered)
            } else {
                onQueryChanged(input)
            }
        },
        label = { Text(text = stringResource(R.string.search_release)) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_release),
            )
        },
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = if (state.isYearFilter) KeyboardType.Number else KeyboardType.Unspecified,
            ),
        keyboardActions = KeyboardActions(onSearch = { onSearchSubmitted() }),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        FilterChip(
            selected = state.isYearFilter,
            onClick = onYearFilterSelected,
            label = { Text(text = "By year") },
        )

        FilterChip(
            selected = state.isGenreFilter,
            onClick = onGenreFilterSelected,
            label = { Text(text = "By genre") },
        )

        FilterChip(
            selected = state.isLabelFilter,
            onClick = onLabelFilterSelected,
            label = { Text(text = "By label") },
        )
    }
}

@Composable
private fun ReleaseCard(
    release: ArtistReleaseModel,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp),
        ) {
            if (release.thumbnailUrl.isNotEmpty()) {
                AsyncImage(
                    model = release.thumbnailUrl,
                    contentDescription = release.title,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(4.dp)),
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Text(
                        text = stringResource(R.string.no_image),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            ReleaseInfo(
                release = release,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(12.dp),
            )
        }
    }
}

@Composable
private fun ReleaseInfo(
    release: ArtistReleaseModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = release.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = release.year,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "-",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = release.type,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (release.genre.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = release.genre.joinToString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReleaseContentPreview() {
    ReleaseContent(
        state = ReleaseState(query = "2009", isYearFilter = true),
        releases =
            flowOf(
                PagingData.from(
                    listOf(
                        ArtistReleaseModel(
                            id = 7283490,
                            title = "Paramore - Paramore Вкл. Brand New Eyes 2009 MP3",
                            year = "2009",
                            type = "release",
                            thumbnailUrl = "",
                            label = listOf("Ice Records"),
                            genre = listOf("Rock"),
                        ),
                        ArtistReleaseModel(
                            id = 2510632,
                            title = "Paramore - Ignorance",
                            year = "2009",
                            type = "release",
                            thumbnailUrl = "",
                            label = listOf("Fueled By Ramen"),
                            genre = listOf("Rock"),
                        ),
                    ),
                ),
            ).collectAsLazyPagingItems(),
        navigateBack = {},
        onQueryChanged = {},
        onSearchSubmitted = {},
        onYearFilterSelected = {},
        onGenreFilterSelected = {},
        onLabelFilterSelected = {},
    )
}
