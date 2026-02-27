package com.example.discogsapp

import android.net.Uri
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
                    startDestination = MAIN_ROUTE,
                ) {
                    composable(route = MAIN_ROUTE) {
                        MainRoute(
                            onArtistSelected = { artist ->
                                val destination = "$DETAIL_ROUTE/${artist.id}/${Uri.encode(artist.title)}/${Uri.encode(artist.thumbnailUrl)}"
                                navController.navigate(destination)
                            },
                        )
                    }

                    composable(
                        route = "$DETAIL_ROUTE/{artistId}/{artistName}/{artistThumbnail}",
                        arguments = listOf(
                            navArgument("artistId") { type = NavType.IntType },
                            navArgument("artistName") { type = NavType.StringType },
                            navArgument("artistThumbnail") { type = NavType.StringType },
                        ),
                    ) {
                        DetailRoute()
                    }
                }
            }
        }
    }

    private companion object {
        const val MAIN_ROUTE = "main"
        const val DETAIL_ROUTE = "detail"
    }
}
