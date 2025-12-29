package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun MetadataSP(
    artistName: String,
    songTitle: String,
    width: Float = 256f,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(width.dp)
        ,
    ) {
        AlbumArtSongPlay(
            size = width
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            ,
        ) {
            SongTitleSP(
                title = songTitle
            )
            Spacer(modifier = Modifier.height(8.dp))
            ArtistNameSongPlay(
                artistName = artistName,
            )
        }
    }
}

@Preview
@Composable
private fun MetadataSPPreview() {
    PreviewColumn {
        MetadataSP(
            artistName = "Tml Vibez",
            songTitle = "365 days"
        )
    }
}