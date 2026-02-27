package com.example.discogsapp.navigation

import android.net.Uri

sealed interface AppRoute {
    val route: String

    data object Main : AppRoute {
        override val route: String = "main"
    }

    data class Detail(
        val artistId: Int,
        val artistName: String,
        val artistThumbnail: String,
    ) : AppRoute {
        override val route: String = "$baseRoute/$artistId/${Uri.encode(artistName)}/${Uri.encode(artistThumbnail)}"

        companion object {
            const val baseRoute = "detail"
            const val artistIdArg = "artistId"
            const val artistNameArg = "artistName"
            const val artistThumbnailArg = "artistThumbnail"

            val routePattern: String = "$baseRoute/{$artistIdArg}/{$artistNameArg}/{$artistThumbnailArg}"
        }
    }
}
