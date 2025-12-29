package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongPlaying(
    modifier: Modifier = Modifier,
    artistName: String,
    songTitle: String,
) {
    val width = 256f
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MetadataSP(
            artistName = artistName,
            songTitle = songTitle,
            width = width,
        )
        Spacer(modifier = Modifier.height(48.dp))
        ControlSectionSongPlay(
            width = width,
        )
    }
}

@Preview
@Composable
private fun SongPlayingPreview() {
    PreviewColumn {
        SongPlaying(
            artistName = "LADIPOE (feat. Ayo Maff)",
            songTitle = "Tension",
        )
    }
}