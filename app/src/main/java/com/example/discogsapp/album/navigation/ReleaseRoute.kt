package com.example.discogsapp.album.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.discogsapp.album.compose.ReleaseScreen
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseRoute(
    val artist: String,
)

fun NavController.openRelease(args: ReleaseRoute) {
    navigate(route = args)
}

fun NavGraphBuilder.releaseScreen(
    navigateBack: () -> Unit,
) = composable<ReleaseRoute> {
    val args = it.toRoute<ReleaseRoute>()
    ReleaseScreen(
        artist = args.artist,
        navigateBack = navigateBack,
    )
}
