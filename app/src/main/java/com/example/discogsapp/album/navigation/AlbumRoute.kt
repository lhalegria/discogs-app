package com.example.discogsapp.album.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.discogsapp.album.compose.AlbumScreen
import kotlinx.serialization.Serializable

@Serializable
data class AlbumRoute(
    val artistId: Int,
)

fun NavController.openAlbum(args: AlbumRoute) {
    navigate(route = args)
}

fun NavGraphBuilder.albumScreen(
    navigateBack: () -> Unit,
) = composable<AlbumRoute> {
    val args = it.toRoute<AlbumRoute>()
    AlbumScreen(
        artistId = args.artistId,
        navigateBack = navigateBack,
    )
}
