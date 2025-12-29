package com.example.sounds.ui.components.song_playing

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsOrion

@Composable
fun SongTitleSP(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = tsOrion
            .copy(fontWeight = FontWeight.SemiBold),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SongTitleSPPreview() {
    PreviewColumn {
        SongTitleSP(
            title = "23"
        )
    }
}