package com.example.sounds

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.sounds.data.local.SoundsDb
import com.example.sounds.data.remote.SoundsApiClient
import com.example.sounds.data.remote.SoundsApiDataSource
import com.example.sounds.data.repository.SoundsRepository
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.SongViewModelFactory
import com.example.sounds.ui.screens.HomeScreen
import com.example.sounds.ui.theme.SoundsTheme
import com.example.sounds.ui.theme.colorKDB

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
            soundsDS = SoundsApiDataSource(
                SoundsApiClient.soundsApi
            ),
            context = applicationContext
        )

        val songViewModel: SongViewModel by viewModels {
            SongViewModelFactory(
                application,
                repository,
            )
        }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                colorKDB.toArgb()
            )
        )
        setContent {
            // TODO implement event channels across screens
            SoundsTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ){
                    HomeScreen(
                        songViewModel = songViewModel,
                    )
                }
            }
        }
    }
}