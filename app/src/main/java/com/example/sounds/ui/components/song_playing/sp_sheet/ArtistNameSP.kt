package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsHush

@Composable
fun ArtistNameSongPlay(
    artistName: String,
    modifier: Modifier = Modifier,
) {
    val ts = tsHush
    Text(
        text = artistName,
        style = ts
            .copy(fontSize = ts.fontSize * 1.2),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ArtistNameSongPlayPreview() {
    PreviewColumn {
        ArtistNameSongPlay(
            artistName = "Burna Boy"
        )
    }
}