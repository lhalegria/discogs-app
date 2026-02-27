package com.example.discogsapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.discogsapp.main.compose.MainRoute as MainScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

fun NavController.openMain() {
    navigate(route = MainRoute)
}

fun NavGraphBuilder.mainScreen(
    navigateToDetail: (DetailRoute) -> Unit,
) = composable<MainRoute> {
    MainScreenRoute(
        onArtistSelected = { artist ->
            navigateToDetail(
                DetailRoute(
                    artistId = artist.id,
                    artistName = artist.title,
                    artistThumbnail = artist.thumbnailUrl,
                )
            )
        },
    )
}
