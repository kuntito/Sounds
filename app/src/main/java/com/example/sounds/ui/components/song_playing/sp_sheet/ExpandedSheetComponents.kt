package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.ControlSectionSongPlay
import com.example.sounds.ui.components.song_playing.sp_queue.SongPlayingQueueSheet
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ExpandedSheetComponents(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    songQueue: List<Song>,
) {
    playerState.currentSong?.let { currentSong ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
            ,
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            SongTitleSP(
                title = currentSong.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            ArtistNameSongPlay(
                artistName = currentSong.artistName,
            )
            Spacer(modifier = Modifier.height(48.dp))
            ControlSectionSongPlay(
                playerState = playerState,
                onPlay = onPlay,
                onPause = onPause,
                onSeekTo = onSeekTo,
                onNext = onNext,
                onPrev = onPrev,
            )
        }
    }
}

@Preview
@Composable
private fun ExpandedSheetComponentsPreview() {
    PreviewColumn {
        val currentSong = dummySongList[2]
        ExpandedSheetComponents(
            playerState = PlayerState(
                currentSong = currentSong,
                isPlaying = true,
            ),
            onPlay = {},
            onPause = {},
            onSeekTo = {},
            onNext = {},
            onPrev = {},
            songQueue = dummySongList,
        )
    }
}