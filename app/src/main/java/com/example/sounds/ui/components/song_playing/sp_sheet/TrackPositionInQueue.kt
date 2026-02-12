package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorMarcelo

@Composable
fun TrackPositionInQueue(
    modifier: Modifier = Modifier,
    currentTrackNumber: Int,
    totalTracks: Int
) {
    Text(
        text = "${currentTrackNumber}/${totalTracks}",
        style = TextStyle(
            color = colorMarcelo,
            fontSize = 10.sp,
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun TrackPositionInQueuePreview() {
    PreviewColumn {
        TrackPositionInQueue(
            currentTrackNumber = 3,
            totalTracks = 12
        )
    }
}