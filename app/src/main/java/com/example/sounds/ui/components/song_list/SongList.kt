package com.example.sounds.ui.components.song_list

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_list_item.SongListItem
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    currentSong: Song?,
    songList: List<Song>,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    playerState: PlayerState,
    onSongItemClick: (Int, List<Song>) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        item {
            Spacer(
                modifier = Modifier
                    .height(topEdgePadding.dp)
            )
        }
        itemsIndexed(songList) { index, song ->
            SongListItem(
                title = song.title,
                artistName = song.artistName,
                isSongPlaying = currentSong == song && playerState.isPlaying,
                albumArtFilePath = song.albumArtFilePath,
                onClick = {
                    onSongItemClick(index, songList)
                }
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(bottomEdgePadding.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SongListPreview() {
    PreviewColumn {
        SongList(
            currentSong = null,
            songList = dummySongList,
            topEdgePadding = 0f,
            bottomEdgePadding = 0f,
            playerState = PlayerState(),
            onSongItemClick = { _, _ -> }
        )
    }
}