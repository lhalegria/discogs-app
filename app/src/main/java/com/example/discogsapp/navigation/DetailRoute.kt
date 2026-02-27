package com.example.discogsapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.discogsapp.detail.compose.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(
    val artistId: Int,
    val artistName: String,
    val artistThumbnail: String,
)

fun NavController.openDetail(args: DetailRoute) {
    navigate(route = args)
}

fun NavGraphBuilder.detailsScreen(
    navigateToAlbums: (AlbumRoute) -> Unit,
) = composable<DetailRoute> {
    val args = it.toRoute<DetailRoute>()
    DetailScreen(
        artistId = args.artistId,
        artistName = args.artistName,
        artistThumbnail = args.artistThumbnail,
        navigateToAlbum = navigateToAlbums,
    )
}
