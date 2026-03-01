package com.example.discogsapp.detail.viewmodel

import com.example.discogsapp.release.navigation.ReleaseRoute
import com.example.discogsapp.viewmodel.flow.UIEffect

sealed class DetailEffect : UIEffect {
    data class NavigateToRelease(
        val releaseRoute: ReleaseRoute,
    ) : DetailEffect()

    data class OpenUrl(
        val url: String,
    ) : DetailEffect()
}
