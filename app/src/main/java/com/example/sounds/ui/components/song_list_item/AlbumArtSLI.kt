package com.example.sounds.ui.components.song_list_item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.AlbumArtFP
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorTelli
import kotlinx.coroutines.delay

@Composable
fun AlbumArtSLI(
    size: Int = 32,
    isSongPlaying: Boolean = false,
    albumArtFilePath: String?,
    modifier: Modifier = Modifier,
) {
    val boxShape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .border(
                width = 0.1.dp,
                color = colorTelli,
                shape = boxShape
            )
            .clip(shape = boxShape)
            .size(size.dp)
    ) {
        AlbumArtFP(
            filePath = albumArtFilePath,
            modifier = Modifier
                .fillMaxSize()
        )

        var delayedVisibility by remember { mutableStateOf(false) }
        LaunchedEffect(isSongPlaying) {
            if (isSongPlaying) {
                delay(200) // slight delay before sound bars appear
                delayedVisibility = true
            } else {
                delayedVisibility = false
            }
        }


        AnimatedVisibility(
            visible = delayedVisibility,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 4.dp,
                    bottom = 4.dp,
                )
        ) {
            SoundBars(
                barColor = colorTelli,
            )
        }
    }
}

@Preview
@Composable
private fun AlbumArtSLIPreview() {
    PreviewColumn {
        var isSongPlaying by remember { mutableStateOf(false) }
        AlbumArtSLI(
            size = 32,
            isSongPlaying = isSongPlaying,
            albumArtFilePath = null,
            modifier = Modifier
                .clickable(
                    enabled = true,
                    onClick = {
                        isSongPlaying = !isSongPlaying
                    }
                )
        )
    }
}