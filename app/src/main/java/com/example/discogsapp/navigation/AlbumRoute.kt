package com.example.discogsapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.discogsapp.album.compose.AlbumScreen
import kotlinx.serialization.Serializable

@Serializable
data class AlbumRoute(
    val artistId: Int,
)

fun NavController.openAlbum(args: AlbumRoute) {
    navigate(route = args)
}

fun NavGraphBuilder.albumScreen() = composable<AlbumRoute> {
    AlbumScreen()
}
