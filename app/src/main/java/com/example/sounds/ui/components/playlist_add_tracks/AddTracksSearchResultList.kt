package com.example.sounds.ui.components.playlist_add_tracks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.components.utils.ShrinkableList

@Composable
fun AddTracksSearchResultList(
    modifier: Modifier = Modifier,
    songSearchResults: List<Song>,
    onAddTrack: (Song) -> Unit,
) {
    fun getDisplayMessageOnShrink(song: Song) = "${song.title}, added"

    ShrinkableList(
        items = songSearchResults,
        getId = { song -> song.id },
        onRemove = onAddTrack,
        topEdgePadding = 16f,
        bottomEdgePadding = 16f,
        getDisplayMessageOnShrink = ::getDisplayMessageOnShrink,
        modifier = modifier,
    ) { song ->
        SearchSongListItem(
            title = song.title,
            artistName = song.artistName,
            albumArtFilePath = song.albumArtFilePath,
        )
    }
}

@Preview
@Composable
private fun AddTracksSearchResultListPreview() {
    PreviewColumn() {
        AddTracksSearchResultList(
            songSearchResults = dummySongList,
            onAddTrack = {}
        )
    }
}