package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorSane

@Composable
fun AlbumArtSongPlay(
    modifier: Modifier = Modifier,
    size: Float = 256f,
) {
    val boxShape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = colorSane,
                shape = boxShape
            )
            .clip(boxShape)
            .size(size.dp)
        ,
    ) {
        Image(
            painter = painterResource(R.drawable.img_album_art),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun AlbumArtSongPlayPreview() {
    PreviewColumn {
        AlbumArtSongPlay()
    }
}