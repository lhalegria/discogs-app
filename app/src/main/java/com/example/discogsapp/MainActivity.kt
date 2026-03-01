package com.example.discogsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
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
                ConfigureStatusBar()
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

    @Composable
    private fun ConfigureStatusBar() {
        val isDarkTheme = isSystemInDarkTheme()
        val view = LocalView.current

        SideEffect {
            window.statusBarColor = if (isDarkTheme) Color.White.toArgb() else Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkTheme
        }
    }
}
