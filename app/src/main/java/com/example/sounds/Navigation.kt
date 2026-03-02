package com.example.sounds

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.screens.PlaylistAddTracksScreenRoot
import com.example.sounds.ui.screens.PlaylistViewScreenRoot
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
                goToPlaylistAddTracksScreen = {
                    navController.navigate(
                        Screens.PlaylistAddTracksScreen
                    )
                },
                goToPlaylistViewScreen = { playlistId ->
                    navController.navigate(Screens.PlaylistViewScreen(playlistId))
                    Log.d(soundsDebugTag, "go to playlist view clicked")
                }
            )
        }
        composable<Screens.PlaylistAddTracksScreen>{
            PlaylistAddTracksScreenRoot(
                songViewModel = songViewModel,
                goToPreviousScreen = {
                    navController.popBackStack()
                },
            )
        }
        composable<Screens.PlaylistViewScreen>{ entry ->
            val screen = entry.toRoute<Screens.PlaylistViewScreen>()
            PlaylistViewScreenRoot(
                playlistId = screen.playlistId,
                songViewModel = songViewModel,
                goToPreviousScreen = {
                    navController.popBackStack()
                },
                goToPlaylistAddTracksScreen = {
                    navController.navigate(
                        Screens.PlaylistAddTracksScreen
                    )
                }
            )
        }
    }
}

sealed class Screens {
    @Serializable
    object HomeScreen

    @Serializable
    object PlaylistAddTracksScreen

    @Serializable
    data class PlaylistViewScreen(val playlistId: Long)
}