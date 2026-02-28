package com.example.discogsapp.detail.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.discogsapp.album.navigation.AlbumRoute
import com.example.discogsapp.detail.viewmodel.DetailEffect
import com.example.discogsapp.detail.viewmodel.DetailViewModel
import com.example.discogsapp.viewmodel.flow.collectAsEffect

@Composable
fun DetailScreen(
    artistId: Int,
    navigateToAlbum: (AlbumRoute) -> Unit,
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uriHandler = LocalUriHandler.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.effect.collectAsEffect {
        when (it) {
            is DetailEffect.NavigateToAlbum -> navigateToAlbum(it.albumRoute)
            is DetailEffect.OpenUrl -> uriHandler.openUri(it.url)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchArtistDetails(artistId)
    }

    BackHandler { navigateBack() }

    DetailContent(
        state = state,
        onClickAlbum = viewModel::navigateToAlbum,
        onRetry = viewModel::retry,
        onClickLink = viewModel::openUrl,
        onNavigateBack = navigateBack,
    )
}
