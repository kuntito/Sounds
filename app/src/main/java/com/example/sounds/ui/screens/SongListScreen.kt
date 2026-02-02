package com.example.sounds.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongPlayingScreen(
    songs: List<Song>,
    playerState: PlayerState,
    playSong: (Song) -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {

    val miniPlayerHeight = 48
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongList(
            songs = songs,
            topEdgePadding = 16f,
            bottomEdgePadding = miniPlayerHeight * 1.2f,
            playerState = playerState,
            playSong = playSong,
        )
        AnimatedVisibility(
            visible = playerState.currentSong != null,
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            SongPlayingSheet(
                miniPlayerHeight = miniPlayerHeight,
                playerState = playerState,
                onPlay = playSong,
                onPause = onPause,
                onSeekTo = onSeekTo,
            )
        }
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
            playerState = PlayerState(),
            playSong = {},
            onPause = {},
            onSeekTo = {},
        )
    }
}