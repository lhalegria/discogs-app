package com.example.discogsapp.main.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    onArtistSelected: (ArtistSummaryModel) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    MainContent(
        state = state,
        onQueryChanged = viewModel::onQueryChanged,
        onSearchSubmitted = viewModel::onSearchSubmitted,
        onArtistSelected = onArtistSelected,
    )
}
