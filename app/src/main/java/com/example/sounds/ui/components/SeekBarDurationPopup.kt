package com.example.sounds.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.sounds.ui.components.utils.PreviewColumn

private fun formatMsToMinSec(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "%d:%02d".format(minutes, seconds)
}

@Composable
fun SeekBarDurationPopup(
    verticalOffset: Int,
    progress: Float,
    audioDuration: Int,
) {

    val currentMs = (progress * audioDuration).toInt()
    val timeText = formatMsToMinSec(currentMs)

    val density = LocalDensity.current

    Popup(
        alignment = Alignment.TopCenter,
        offset = with(density) {
            IntOffset(
                x = 0,
                y = verticalOffset.dp.roundToPx()
            )
        },
    ) {
        Text(
            text = timeText,
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Preview
@Composable
private fun SeekBarDurationPopupPreview() {
    PreviewColumn {
        SeekBarDurationPopup(
            verticalOffset = -40,
            progress = 0.5f,
            audioDuration = 180000,
        )
    }
}