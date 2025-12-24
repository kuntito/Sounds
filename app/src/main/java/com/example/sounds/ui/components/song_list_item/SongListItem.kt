package com.example.sounds.ui.components.song_list_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn


@Composable
fun SongListItem(
    title: String,
    artistName: String,
    modifier: Modifier = Modifier,
) {
    ClickableSurface(
        onClick = {},
        isRippleBounded = true,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
            ,
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            AlbumArtSLI()
            Spacer(modifier = Modifier.width(16.dp))
            SongTitleAndArtistName(
                title = title,
                artistName = artistName,
                modifier = modifier
                    .weight(1f)
                ,
            )
            Spacer(modifier = Modifier.width(16.dp))
            AppIconButton(
                iconRes = R.drawable.ic_more_vert
            ) { }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun SongTitleAndArtistName(
    title: String,
    artistName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TitleSLI(title = title)
        Spacer(modifier = Modifier.height(2.dp))
        ArtistNameSLI(artistName = artistName)
    }
}

@Preview
@Composable
private fun SongListItemPreview() {
    PreviewColumn() {
        SongListItem(
            title = "Monica Lewinsky",
            artistName = "SAINt JHN"
        )
        SongListItem(
            title = "Monica Lewinsky",
            artistName = "SAINt JHN"
        )
    }
}