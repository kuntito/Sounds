package com.example.sounds.ui.components.song_list_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongTitleAndArtistName(
    modifier: Modifier = Modifier,
    title: String,
    artistName: String,
    isSongPlaying: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TitleSLI(
            title = title,
            isSongPlaying = isSongPlaying,
        )
        Spacer(modifier = Modifier.height(2.dp))
        ArtistNameSLI(
            artistName = artistName,
            isSongPlaying = isSongPlaying,
        )
    }
}