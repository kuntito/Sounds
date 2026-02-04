package com.example.sounds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.local.SoundsDb
import com.example.sounds.data.remote.SoundsApiClient
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.SongViewModelFactory
import com.example.sounds.ui.screens.SongPlayingScreen
import com.example.sounds.ui.theme.SoundsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dependency setup
        val db = SoundsDb.getDatabase(applicationContext)
        val repository = SoundsRepository(
            songDao = db.songDao(),
            soundsApi = SoundsApiClient.soundsApi,
            context = applicationContext
        )

        val songViewModel: SongViewModel by viewModels { SongViewModelFactory(repository) }

        enableEdgeToEdge()
        setContent {
            val songs by songViewModel.songs.collectAsState()
            val playerState by songViewModel.playerState.collectAsState()
            val songQueue by songViewModel.songQueue.collectAsState()

            SoundsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SongPlayingScreen(
                        songs = songs,
                        playerState = playerState,
                        playSong = songViewModel::playSong,
                        onSongItemClick = songViewModel::onSongItemClick,
                        onPause = songViewModel::pauseSong,
                        onSeekTo = songViewModel::seekSongTo,
                        onNext = songViewModel::onNextSong,
                        onPrev = songViewModel::onPreviousSong,
                        songQueue = songQueue,
                        onSwapSong = songViewModel::onSwapSong,
                        modifier = Modifier
                            .padding(innerPadding),
                    )
                }
            }
        }
    }
}