package com.example.sounds.ui.components.song_playing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.R
import com.example.sounds.player.PlaybackRepeatModes
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun RepeatBtn(
    modifier: Modifier = Modifier,
    repeatMode: PlaybackRepeatModes,
    toggleRepeat: () -> Unit
) {

    val iconRes = when (repeatMode) {
        PlaybackRepeatModes.NoRepeat -> R.drawable.ic_repeat_off
        PlaybackRepeatModes.RepeatOne -> R.drawable.ic_repeat_one
        PlaybackRepeatModes.RepeatAll -> R.drawable.ic_repeat_all
    }
    AppIconButton(
        iconRes = iconRes,
        modifier = modifier,
    ) {
        toggleRepeat()
    }
}

@Preview
@Composable
private fun RepeatBtnPreview() {
    PreviewColumn() {
        RepeatBtn(
            repeatMode = PlaybackRepeatModes.NoRepeat,
            toggleRepeat = {}
        )
    }
}