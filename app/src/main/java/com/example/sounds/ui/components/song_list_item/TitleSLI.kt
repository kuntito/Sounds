package com.example.sounds.ui.components.song_list_item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsOrion

@Composable
fun TitleSLI(
    title: String,
    isSongPlaying: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val ts = tsOrion
    val fontWeight = if (isSongPlaying) FontWeight.SemiBold else ts.fontWeight
    Text(
        text = title,
        style = ts
            .copy(
                fontWeight = fontWeight
            ),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun TitleSLIPreview() {
    PreviewColumn() {
        TitleSLI("Monica Lewinsky")
    }
}