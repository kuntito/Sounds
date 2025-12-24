package com.example.sounds.ui.components.song_list_item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsOrion

@Composable
fun TitleSLI(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = tsOrion,
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