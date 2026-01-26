package com.example.sounds.ui.components.song_playing

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn

enum class PlayPauseState(
    @get:DrawableRes val iconRes: Int,
) {
    Play(R.drawable.ic_play),
    Pause(R.drawable.ic_pause)
}

@Composable
fun PlayPauseBtn(
    modifier: Modifier = Modifier,
    size: Int = 24,
) {
    var state by remember { mutableStateOf(PlayPauseState.Play) }
    val toggleState = {
        state = when(state) {
            PlayPauseState.Play -> PlayPauseState.Pause
            PlayPauseState.Pause -> PlayPauseState.Play
        }
    }

    AppIconButton(
        iconRes = state.iconRes,
        size = size,
        modifier = modifier,
    ) {
        toggleState()
    }
}

@Preview
@Composable
private fun PlayPauseBtnPreview() {
    PreviewColumn() {
        PlayPauseBtn()
    }
}