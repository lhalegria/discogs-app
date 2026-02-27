package com.example.discogsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.discogsapp.navigation.MainRoute
import com.example.discogsapp.navigation.albumScreen
import com.example.discogsapp.navigation.detailsScreen
import com.example.discogsapp.navigation.mainScreen
import com.example.discogsapp.navigation.openAlbum
import com.example.discogsapp.navigation.openDetail
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
                    startDestination = MainRoute,
                ) {
                    mainScreen(navigateToDetail = navController::openDetail)
                    detailsScreen(navigateToAlbums = navController::openAlbum)
                    albumScreen()
                }
            }
        }
    }
}
