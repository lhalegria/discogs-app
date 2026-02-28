package com.example.discogsapp.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.discogsapp.album.navigation.AlbumRoute
import com.example.discogsapp.detail.compose.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(
    val artistId: Int,
)

fun NavController.openDetail(args: DetailRoute) {
    navigate(route = args)
}

fun NavGraphBuilder.detailsScreen(
    navigateToAlbums: (AlbumRoute) -> Unit,
    navigateBack: () -> Unit,
) = composable<DetailRoute> {
    val args = it.toRoute<DetailRoute>()
    DetailScreen(
        artistId = args.artistId,
        navigateToAlbum = navigateToAlbums,
        navigateBack = navigateBack,
    )
}
