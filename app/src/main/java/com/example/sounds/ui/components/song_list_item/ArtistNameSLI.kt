package com.example.sounds.ui.components.song_list_item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsHush

@Composable
fun ArtistNameSLI(
    artistName: String,
    isSongPlaying: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val ts = tsHush
    val fontWeight = if (isSongPlaying) FontWeight.Normal else ts.fontWeight
    Text(
        text = artistName,
        style = ts
            .copy(fontWeight = fontWeight),
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