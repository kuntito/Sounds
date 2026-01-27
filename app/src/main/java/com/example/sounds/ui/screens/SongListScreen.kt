package com.example.sounds.ui.screens

import android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongPlayingScreen(
    modifier: Modifier = Modifier,
) {
    // TODO start here.. mini player should appear with `colorAguero` background,
    //  the moment it expands, switch to `colorKDB`

    // TODO add the mini player components..
    //  see `MiniPlayerSP`
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongList(songs = dummySongList)
        SongPlayingSheet(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun SongPlayingScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {
        SongPlayingScreen()
    }
}