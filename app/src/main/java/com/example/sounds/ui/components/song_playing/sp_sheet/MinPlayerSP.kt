package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.song_playing.PlayPauseBtn
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.components.utils.topShadow
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.tsHush
import com.example.sounds.ui.theme.tsOrion


@Composable
fun MiniPlayerSongPlaying(
    heightDp: Int,
    horizontalPaddingDp: Int,
    modifier: Modifier = Modifier,
) {
    // TODO, don't harcode
    val songTitle = "Trees"
    val artistName = "Olivetheboy"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .topShadow(shadowHeight = 20f)
            .background(colorAguero)
        ,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            Text(
                text = songTitle,
                style = tsOrion,
            )
            Text(
                text = artistName,
                style = tsHush,
            )
        }
        PlayPauseBtn(
            size = 16
        )
        Spacer(modifier = Modifier.width(horizontalPaddingDp.dp))
    }
}

@Preview
@Composable
private fun MiniPlayerSongPlayingPreview() {
    PreviewColumn() {
        MiniPlayerSongPlaying(
            heightDp = 48,
            horizontalPaddingDp = 16
        )
    }
}