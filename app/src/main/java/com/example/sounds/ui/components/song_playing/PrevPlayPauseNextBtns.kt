package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PrevPlayPauseNextBtns(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    // play/pause icon is visually left-heavy
    // spacers experimentally adjusted to compensate
    val leftSpacer = 28
    val rightSpacer = 24

    Row(
        modifier = modifier,
    ) {
        AppIconButton(
            iconRes = R.drawable.ic_prev
        ) { }
        Spacer(modifier = Modifier.width(leftSpacer.dp))
        PlayPauseBtn(
            isPlaying = playerState.isPlaying,
            onPause = onPause,
            onPlay = onPlay,
        )
        Spacer(modifier = Modifier.width(rightSpacer.dp))
        AppIconButton(
            iconRes = R.drawable.ic_next
        ) { }
    }
}

@Preview
@Composable
private fun PrevPlayPauseNextBtnsPreview() {
    PreviewColumn() {
        PrevPlayPauseNextBtns(
            playerState = PlayerState(),
            onPlay = {},
            onPause = {},
        )
    }
}