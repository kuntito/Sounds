package com.example.sounds.ui.components.song_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySong
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.song_list_item.SongListItem
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        items(songs) { song ->
            SongListItem(
                title = song.title,
                artistName = song.artistName,
            )
        }
    }
}

@Preview
@Composable
private fun SongListPreview() {
    PreviewColumn {
        SongList(
            songs = dummySongList
        )
    }
}