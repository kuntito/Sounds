package com.example.sounds.ui.components.playlist_list

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
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.ui.components.song_list_item.AlbumArtSLI
import com.example.sounds.ui.components.song_list_item.SongTitleAndArtistName
import com.example.sounds.ui.components.utils.AppDropdownMenu
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistSongListItem(
    modifier: Modifier = Modifier,
    title: String,
    artistName: String,
    albumArtFilePath: String?,
    playlistSongMenuOptions: List<DropdownMenuOption>,
    onClick: () -> Unit,
) {
    // don't want to indicate currently playing song while viewing playlist
    val isSongPlaying = false
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
        ,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
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
            AppDropdownMenu(dropdownOptions = playlistSongMenuOptions)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview
@Composable
private fun PlaylistSongListItemPreview() {
    PreviewColumn() {
        val dropdownOptions = listOf(
            DropdownMenuOption(
                label = "delete",
                onClick = {},
            )
        )

        PlaylistSongListItem(
            title = "Stronger Than I Was",
            artistName = "Eminem",
            albumArtFilePath = null,
            playlistSongMenuOptions = dropdownOptions,
            onClick = {},
        )
        PlaylistSongListItem(
            title = "Understand",
            artistName = "Omah Lay",
            albumArtFilePath = null,
            playlistSongMenuOptions = dropdownOptions,
            onClick = {},
        )
    }
}