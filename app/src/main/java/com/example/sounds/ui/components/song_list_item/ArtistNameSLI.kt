package com.example.sounds.ui.components.song_list_item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsHush

@Composable
fun ArtistNameSLI(
    artistName: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = artistName,
        style = tsHush,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ArtistNameSLIPreview() {
    PreviewColumn {
        ArtistNameSLI("SAINt JHN")
    }
}