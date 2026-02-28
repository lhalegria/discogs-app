package com.example.discogsapp.album.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.discogsapp.R
import com.example.discogsapp.album.viewmodel.AlbumState
import com.example.discogsapp.common.compose.ErrorState
import com.example.discogsapp.common.compose.LoadingState
import com.example.discogsapp.common.compose.TopBar
import com.example.discogsapp.domain.model.ArtistReleaseModel
import kotlinx.coroutines.flow.distinctUntilChanged

private const val LOAD_MORE_THRESHOLD = 3

private enum class AlbumContentState {
    Loading,
    Error,
    Success,
}

@Composable
fun AlbumContent(
    state: AlbumState,
    onFilterByYear: (String) -> Unit,
    onFilterByType: (String) -> Unit,
    onFilterByLabel: (String) -> Unit,
    onClearFilters: () -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val contentState =
        when {
            state.isError() -> AlbumContentState.Error
            state.isSuccessful() -> AlbumContentState.Success
            else -> AlbumContentState.Loading
        }

    AnimatedContent(
        targetState = contentState,
        label = "album-content-state",
        modifier = Modifier.fillMaxSize(),
    ) { targetState ->
        when (targetState) {
            AlbumContentState.Loading -> {
                LoadingState(message = stringResource(R.string.loading_albums))
            }

            AlbumContentState.Error -> {
                ErrorState(
                    errorMessage = stringResource(R.string.error_loading_albums),
                    isButtonEnabled = !state.isLoading,
                    onRetry = onRetry,
                    onGoBack = onNavigateBack,
                )
            }

            AlbumContentState.Success -> {
                AlbumSuccessContent(
                    state = state,
                    onFilterByYear = onFilterByYear,
                    onFilterByType = onFilterByType,
                    onFilterByLabel = onFilterByLabel,
                    onClearFilters = onClearFilters,
                    onLoadMore = onLoadMore,
                    onNavigateBack = onNavigateBack,
                )
            }
        }
    }
}

@Composable
private fun AlbumSuccessContent(
    state: AlbumState,
    onFilterByYear: (String) -> Unit,
    onFilterByType: (String) -> Unit,
    onFilterByLabel: (String) -> Unit,
    onClearFilters: () -> Unit,
    onLoadMore: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val listState = rememberLazyListState()
    val shouldAutoLoadMore = state.hasMorePages && !state.isLoadingMore

    LaunchedEffect(listState, shouldAutoLoadMore) {
        if (!shouldAutoLoadMore) return@LaunchedEffect
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount
            lastVisibleIndex to totalItems
        }.distinctUntilChanged()
            .collect { (lastVisibleIndex, totalItems) ->
                if (totalItems > 0 && lastVisibleIndex >= totalItems - LOAD_MORE_THRESHOLD) {
                    onLoadMore()
                }
            }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.albums_title),
                onNavigateBack = onNavigateBack,
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
            ) {
                item {
                    FilterSection(
                        selectedYear = state.selectedYear,
                        selectedType = state.selectedType,
                        selectedLabel = state.selectedLabel,
                        availableYears = state.availableYears,
                        availableTypes = state.availableTypes,
                        availableLabels = state.availableLabels,
                        onFilterByYear = onFilterByYear,
                        onFilterByType = onFilterByType,
                        onFilterByLabel = onFilterByLabel,
                        onClearFilters = onClearFilters,
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.albums_count, state.releases.size, state.totalAlbum),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }

                itemsIndexed(
                    items = state.filteredReleases,
                    key = { index, item -> "${item.id}-$index" },
                ) { _, release ->
                    AlbumCard(release = release)
                }

                if (state.isLoadingMore) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.loading_more_albums),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FilterSection(
    selectedYear: String,
    selectedType: String,
    selectedLabel: String,
    availableYears: List<String>,
    availableTypes: List<String>,
    availableLabels: List<String>,
    onFilterByYear: (String) -> Unit,
    onFilterByType: (String) -> Unit,
    onFilterByLabel: (String) -> Unit,
    onClearFilters: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.filters_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
        )

        Filter(
            title = stringResource(R.string.filter_by_year),
            selected = selectedYear,
            available = availableYears,
            onClick = onFilterByYear,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Filter(
            title = stringResource(R.string.filter_by_type),
            selected = selectedType,
            available = availableTypes,
            onClick = onFilterByType,
        )

        Filter(
            title = stringResource(R.string.filter_by_label),
            selected = selectedLabel,
            available = availableLabels,
            onClick = onFilterByLabel,
        )

        if (selectedYear.isNotEmpty() || selectedType.isNotEmpty() || selectedLabel.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onClearFilters,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.clear_filters))
            }
        }
    }
}

@Composable
private fun Filter(
    title: String,
    selected: String,
    available: List<String>,
    onClick: (String) -> Unit,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(bottom = 4.dp),
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            FilterChip(
                selected = selected.isEmpty(),
                onClick = { onClick("") },
                label = { Text(text = stringResource(R.string.filter_all)) },
            )
        }
        items(available) { item ->
            FilterChip(
                selected = selected == item,
                onClick = { onClick(item) },
                label = { Text(text = item) },
            )
        }
    }
}

@Composable
private fun AlbumCard(
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

            AlbumInfo(
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
private fun AlbumInfo(
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
        if (release.role.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = release.role,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumContentSuccessPreview() {
    AlbumContent(
        state =
            AlbumState(
                artistId = 108713,
                releases =
                    listOf(
                        ArtistReleaseModel(
                            id = 1,
                            title = "All the Right Reasons",
                            year = "2005",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                        ArtistReleaseModel(
                            id = 2,
                            title = "Dark Horse",
                            year = "2008",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                        ArtistReleaseModel(
                            id = 3,
                            title = "Silver Side Up",
                            year = "2001",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                    ),
                filteredReleases =
                    listOf(
                        ArtistReleaseModel(
                            id = 1,
                            title = "All the Right Reasons",
                            year = "2005",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                        ArtistReleaseModel(
                            id = 2,
                            title = "Dark Horse",
                            year = "2008",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                        ArtistReleaseModel(
                            id = 3,
                            title = "Silver Side Up",
                            year = "2001",
                            role = "Main",
                            type = "Album",
                            thumbnailUrl = "",
                            label = "Roadrunner",
                        ),
                    ),
                availableYears = listOf("2001", "2005", "2008"),
                availableTypes = listOf("Album", "Single"),
                availableLabels = listOf("Roadrunner"),
            ),
        onFilterByYear = {},
        onFilterByType = {},
        onFilterByLabel = {},
        onClearFilters = {},
        onRetry = {},
        onLoadMore = {},
        onNavigateBack = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AlbumContentLoadingPreview() {
    AlbumContent(
        state = AlbumState(),
        onFilterByYear = {},
        onFilterByType = {},
        onFilterByLabel = {},
        onClearFilters = {},
        onRetry = {},
        onLoadMore = {},
        onNavigateBack = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AlbumContentErrorPreview() {
    AlbumContent(
        state =
            AlbumState(
                artistId = 108713,
                hasError = true,
            ),
        onFilterByYear = {},
        onFilterByType = {},
        onFilterByLabel = {},
        onClearFilters = {},
        onRetry = {},
        onLoadMore = {},
        onNavigateBack = {},
    )
}
