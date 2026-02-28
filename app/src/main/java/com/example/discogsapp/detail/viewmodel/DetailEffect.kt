package com.example.discogsapp.detail.viewmodel

import com.example.discogsapp.album.navigation.AlbumRoute
import com.example.discogsapp.viewmodel.flow.UIEffect

sealed class DetailEffect : UIEffect {
    data class NavigateToAlbum(
        val albumRoute: AlbumRoute,
    ) : DetailEffect()

    data class OpenUrl(
        val url: String,
    ) : DetailEffect()
}
