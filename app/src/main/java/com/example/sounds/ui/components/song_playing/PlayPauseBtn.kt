package com.example.sounds.ui.components.song_playing

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

@Composable
fun PlayPauseBtn(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    size: Int = 24,
) {
    val iconRes = if (isPlaying) R.drawable.ic_pause  else R.drawable.ic_play
    val onClick: () -> Unit = if (isPlaying) onPause else onPlay
    AppIconButton(
        iconRes = iconRes,
        size = size,
        modifier = modifier,
        onClick = onClick
    )
}

@Preview
@Composable
private fun PlayPauseBtnPreview() {
    PreviewColumn() {
        var isPlaying by remember{ mutableStateOf(false) }
        PlayPauseBtn(
            isPlaying = isPlaying,
            onPlay = {
                isPlaying = true
            },
            onPause = {
                isPlaying = false
            }
        )
    }
}