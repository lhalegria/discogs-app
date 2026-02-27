package com.example.discogsapp.detail.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.discogsapp.detail.viewmodel.DetailViewModel
import com.example.discogsapp.album.navigation.AlbumRoute

@Composable
fun DetailScreen(
    artistId: Int,
    artistName: String,
    artistThumbnail: String,
    navigateToAlbum: (AlbumRoute) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    viewModel.bind(artistId, artistName, artistThumbnail)

    DetailContent(
        artistId,
        artistName,
        artistThumbnail,
        navigateToAlbum
    )
}
