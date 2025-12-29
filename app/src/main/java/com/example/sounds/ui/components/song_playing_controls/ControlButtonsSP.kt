package com.example.sounds.ui.components.song_playing_controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ControlButtonsSongPlay(
    modifier: Modifier = Modifier,
    width: Float = 256f,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(width.dp)
        ,
    ) {

        ShuffleBtn()
        PrevPlayPauseNextBtns()
        RepeatBtn()
    }
}


@Preview
@Composable
private fun ControlButtonsSongPlayPreview() {
    PreviewColumn() {
        ControlButtonsSongPlay()
    }
}