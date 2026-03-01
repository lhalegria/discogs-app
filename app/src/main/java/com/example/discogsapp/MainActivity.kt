package com.example.discogsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.discogsapp.detail.navigation.detailsScreen
import com.example.discogsapp.detail.navigation.openDetail
import com.example.discogsapp.main.navigation.MainRoute
import com.example.discogsapp.main.navigation.mainScreen
import com.example.discogsapp.release.navigation.openRelease
import com.example.discogsapp.release.navigation.releaseScreen
import com.example.discogsapp.ui.theme.DiscogsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscogsTheme {
                val navController = rememberNavController()
                DiscogsNavHost(
                    navController = navController,
                    startDestination = MainRoute,
                ) {
                    mainScreen(navigateToDetail = navController::openDetail)
                    detailsScreen(
                        navigateToReleases = navController::openRelease,
                        navigateBack = navController::popBackStack,
                    )
                    releaseScreen(navigateBack = navController::popBackStack)
                }
            }
        }
    }
}
