package com.example.discogsapp.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.discogsapp.detail.navigation.DetailRoute
import com.example.discogsapp.main.compose.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

fun NavController.openMain() {
    navigate(route = MainRoute)
}

fun NavGraphBuilder.mainScreen(navigateToDetail: (DetailRoute) -> Unit) =
    composable<MainRoute> {
        MainScreen(
            onArtistSelected = { artist ->
                navigateToDetail(
                    DetailRoute(artistId = artist.id),
                )
            },
        )
    }
