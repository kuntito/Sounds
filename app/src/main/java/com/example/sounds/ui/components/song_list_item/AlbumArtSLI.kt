package com.example.sounds.ui.components.song_list_item

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorTelli

@Composable
fun AlbumArtSLI(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = colorTelli,
                shape = RoundedCornerShape(8.dp)
            )
            .size(32.dp)
    ) {

    }
}

@Preview
@Composable
private fun AlbumArtSLIPreview() {
    PreviewColumn {
        AlbumArtSLI()
    }
}