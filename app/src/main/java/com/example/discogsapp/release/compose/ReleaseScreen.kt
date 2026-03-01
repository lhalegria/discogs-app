package com.example.discogsapp.release.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.discogsapp.release.viewmodel.ReleaseViewModel

@Composable
fun ReleaseScreen(
    artist: String,
    navigateBack: () -> Unit,
    viewModel: ReleaseViewModel = hiltViewModel(),
) {
    viewModel.setArtist(artist)

    val state by viewModel.state.collectAsState()
    val releases = viewModel.artistReleasePagingData.collectAsLazyPagingItems()

    ReleaseContent(
        state = state,
        releases = releases,
        navigateBack = navigateBack,
        onQueryChanged = viewModel::onQueryChanged,
        onSearchSubmitted = viewModel::onSearchSubmitted,
        onYearFilterSelected = viewModel::setYearFilter,
        onGenreFilterSelected = viewModel::setGenreFilter,
        onLabelFilterSelected = viewModel::setLabelFilter,
    )
}
