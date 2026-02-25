package com.example.sounds

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.screens.PlaylistAddTracksScreenRoot
import com.example.sounds.ui.screens.home_screen.HomeScreenRoot
import kotlinx.serialization.Serializable

// TODO implement event channels across screens
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen,
        modifier = modifier
            .fillMaxSize()
    ) {
        composable<Screens.HomeScreen>{
            HomeScreenRoot(
                songViewModel = songViewModel,
                goToPlaylistAddTracks = {
                    navController.navigate(
                        Screens.PlaylistAddTracksScreen
                    )
                }
            )
        }
        composable<Screens.PlaylistAddTracksScreen>{
            PlaylistAddTracksScreenRoot(
                songViewModel = songViewModel,
            )
        }
    }
}

sealed class Screens {
    @Serializable
    object HomeScreen

    @Serializable
    object PlaylistAddTracksScreen
}