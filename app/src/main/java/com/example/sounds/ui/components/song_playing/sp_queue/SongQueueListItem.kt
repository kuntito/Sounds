package com.example.sounds.ui.components.song_playing.sp_queue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.song_list_item.AlbumArtSLI
import com.example.sounds.ui.components.song_list_item.SongTitleAndArtistName
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorKDB

@Composable
fun SongQueueListItem(
    modifier: Modifier = Modifier,
    title: String,
    artistName: String,
    albumArtFilePath: String?,
    isSongPlaying: Boolean,
    onClick: () -> Unit,
    dragHandleModifier: Modifier,
) {
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
    ) {
        val bgColor = if (isSongPlaying) colorKDB else Color.Transparent
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(bgColor)
            ,
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            AlbumArtSLI(
                isSongPlaying = isSongPlaying,
                albumArtFilePath = albumArtFilePath,
            )
            Spacer(modifier = Modifier.width(16.dp))
            SongTitleAndArtistName(
                title = title,
                artistName = artistName,
                isSongPlaying = isSongPlaying,
                modifier = modifier
                    .weight(1f)
                ,
            )
            Spacer(modifier = Modifier.width(16.dp))
            AppIconButton(
                iconRes = R.drawable.ic_drag_handle,
                modifier = dragHandleModifier,
            ) { }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview
@Composable
private fun SongQueueListItemPreview() {
    PreviewColumn() {
        var playingIndex by remember{ mutableIntStateOf(-1) }

        PreviewColumn() {
            SongQueueListItem(
                title = "Monica Lewinsky",
                artistName = "SAINt JHN",
                albumArtFilePath = null,
                isSongPlaying = playingIndex == 0,
                onClick = { playingIndex = 0 },
                dragHandleModifier = Modifier,
            )
            SongQueueListItem(
                title = "Monica Lewinsky",
                artistName = "SAINt JHN",
                albumArtFilePath = null,
                isSongPlaying = playingIndex == 1,
                onClick = { playingIndex = 1 },
                dragHandleModifier = Modifier,
            )
        }
    }
}