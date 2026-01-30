package com.example.sounds.ui.components.song_playing.sp_sheet

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySong
import com.example.sounds.ui.components.song_playing.PlayPauseBtn
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsHush
import com.example.sounds.ui.theme.tsOrion


@Composable
fun MiniPlayerSongPlaying(
    heightDp: Int,
    endPaddingDp: Int,
    currentSong: Song,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp)
        ,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            Text(
                text = currentSong.title,
                style = tsOrion,
            )
            Text(
                text = currentSong.artistName,
                style = tsHush,
            )
        }
        PlayPauseBtn(
            size = 16,
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
        )
        Spacer(modifier = Modifier.width(endPaddingDp.dp))
    }
}

@Preview
@Composable
private fun MiniPlayerSongPlayingPreview() {
    PreviewColumn() {
        MiniPlayerSongPlaying(
            heightDp = 48,
            endPaddingDp = 16,
            currentSong = dummySong,
            isPlaying = false,
            onPlay = {},
            onPause = {},
        )
    }
}