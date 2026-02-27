package com.example.discogsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.discogsapp.detail.compose.DetailRoute
import com.example.discogsapp.main.compose.MainRoute
import com.example.discogsapp.navigation.AppRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppRoute.Main.route,
                ) {
                    composable(route = AppRoute.Main.route) {
                        MainRoute(
                            onArtistSelected = { artist ->
                                navController.navigate(
                                    AppRoute.Detail(
                                        artistId = artist.id,
                                        artistName = artist.title,
                                        artistThumbnail = artist.thumbnailUrl,
                                    ).route
                                )
                            },
                        )
                    }

                    composable(
                        route = AppRoute.Detail.routePattern,
                        arguments = listOf(
                            navArgument(AppRoute.Detail.artistIdArg) { type = NavType.IntType },
                            navArgument(AppRoute.Detail.artistNameArg) { type = NavType.StringType },
                            navArgument(AppRoute.Detail.artistThumbnailArg) { type = NavType.StringType },
                        ),
                    ) {
                        DetailRoute()
                    }
                }
            }
        }
    }
}
