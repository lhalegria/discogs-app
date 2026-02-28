package com.example.discogsapp.album.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.discogsapp.album.viewmodel.AlbumViewModel

@Composable
fun AlbumScreen(
    artistId: Int,
    navigateBack: () -> Unit,
    viewModel: AlbumViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.fetchArtistReleases(artistId)
    }

    AlbumContent(
        state = state,
        onFilterByYear = viewModel::filterByYear,
        onFilterByType = viewModel::filterByType,
        onFilterByLabel = viewModel::filterByLabel,
        onClearFilters = viewModel::clearFilters,
        onRetry = viewModel::retry,
        onLoadMore = viewModel::loadMore,
        onNavigateBack = navigateBack,
    )
}
