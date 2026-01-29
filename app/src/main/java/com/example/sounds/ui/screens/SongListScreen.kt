package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongPlayingScreen(
    songs: List<Song>,
    currentSongId: String? = null,
    playSong: (Song) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO start here.. mini player should appear with `colorAguero` background,
    //  the moment it expands, switch to `colorKDB`

    // TODO add the mini player components..
    //  see `MiniPlayerSP`
    val miniPlayerHeight = 48
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongList(
            songs = songs,
            topEdgePadding = 16f,
            bottomEdgePadding = miniPlayerHeight * 1.2f,
            currentSongId = currentSongId,
            playSong = playSong,
        )
        SongPlayingSheet(
            miniPlayerHeight = miniPlayerHeight,
            modifier = Modifier
                .align(Alignment.BottomCenter),
        )
    }
}

@Preview
@Composable
private fun SongPlayingScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {
        SongPlayingScreen(
            songs = dummySongList,
            playSong = {}
        )
    }
}