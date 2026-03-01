package com.example.discogsapp.detail.viewmodel

import com.example.discogsapp.album.navigation.ReleaseRoute
import com.example.discogsapp.viewmodel.flow.UIEffect

sealed class DetailEffect : UIEffect {
    data class NavigateToRelease(
        val albumRoute: ReleaseRoute,
    ) : DetailEffect()

    data class OpenUrl(
        val url: String,
    ) : DetailEffect()
}
