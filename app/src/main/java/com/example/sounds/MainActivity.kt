package com.example.sounds

import android.content.Context
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.local.SoundsDb
import com.example.sounds.data.remote.SoundsApiClient
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.SongViewModelFactory
import com.example.sounds.ui.screens.SongPlayingScreen
import com.example.sounds.ui.theme.SoundsTheme

const val soundsDebugTag = "sounds_tag"
val Context.prefDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings",
)
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

        val songViewModel: SongViewModel by viewModels {
            SongViewModelFactory(
                application,
                repository,
            )
        }

        enableEdgeToEdge()
        setContent {
            val songs by songViewModel.songs.collectAsState()
            val playerState by songViewModel.playerState.collectAsState()
            val songQueue by songViewModel.songQueue.collectAsState()
            val currentSong by songViewModel.currentSong.collectAsState()
            val previousSong by songViewModel.previousSong.collectAsState()
            val nextSong by songViewModel.nextSong.collectAsState()
            val isShuffled by songViewModel.isShuffled.collectAsState()
            val currentTrackNumber by songViewModel.currentTrackNumber.collectAsState()

            SoundsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SongPlayingScreen(
                        songs = songs,
                        playerState = playerState,
                        playbackActions = songViewModel.playbackActions,
                        songQueue = songQueue,
                        isShuffled = isShuffled,
                        currentSong = currentSong,
                        prevSongAAFP = previousSong?.albumArtFilePath,
                        nextSongAAFP = nextSong?.albumArtFilePath,
                        currentTrackNumber = currentTrackNumber,
                        modifier = Modifier
                            .padding(innerPadding),
                    )
                }
            }
        }
    }
}