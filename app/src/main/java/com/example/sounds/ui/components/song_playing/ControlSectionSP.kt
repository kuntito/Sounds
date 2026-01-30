package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ControlSectionSongPlay(
    modifier: Modifier = Modifier,
    width: Float = 256f,
    playerState: PlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        ,
    ) {
        SeekBar(
            width = width * 1.07f, // looks better this way
        )
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )
        ControlButtonsSongPlay(
            width = width,
            playerState = playerState,
            onPlay = onPlay,
            onPause = onPause,
        )
    }
}

@Preview
@Composable
private fun ControlSectionSongPlayPreview() {
    PreviewColumn() {
        ControlSectionSongPlay(
            playerState = PlayerState(
                isPlaying = false,
            ),
            onPlay = {},
            onPause = {},
        )
    }
}