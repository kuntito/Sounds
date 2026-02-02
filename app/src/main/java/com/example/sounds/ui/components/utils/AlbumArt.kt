package com.example.sounds.ui.components.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sounds.R
import com.example.sounds.ui.theme.colorTelli
import java.io.File

@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    filePath: String?,
    loadSize: Int? = null,
) {

    val model = if (loadSize == null) {
        filePath?.let{ File(it) }
    } else {
        val maxImageSizePx = with(LocalDensity.current) {
            loadSize.dp.roundToPx()
        }

        ImageRequest.Builder(LocalContext.current)
            .data(filePath?.let { File(it) })
            .size(maxImageSizePx)
            .build()
    }

    val boxShape = RoundedCornerShape(8.dp)

    AsyncImage(
        model = model,
        contentDescription = null,
        placeholder = painterResource(R.drawable.album_art_placeholder),
        error = painterResource(R.drawable.album_art_placeholder),
        fallback = painterResource(R.drawable.album_art_placeholder),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .border(
                width = 0.1.dp,
                color = colorTelli,
                shape = boxShape
            )
            .clip(shape = boxShape)
    )
}

@Preview
@Composable
private fun AlbumArtPreview() {
    PreviewColumn {
        AlbumArt(filePath = null)
    }
}