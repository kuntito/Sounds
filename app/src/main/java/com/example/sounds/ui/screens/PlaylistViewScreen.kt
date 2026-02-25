package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Playlist
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummyPlaylist
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.playlist_view.PlaylistViewHeader
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.utils.millisToMinutes

@Composable
fun PlaylistViewScreen(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    currentSong: Song?,
    playlistSongs: List<Song>,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    playerState: PlayerState,
    onSongItemClick: (Int, List<Song>) -> Unit,
) {
    val playlistDurationMins = millisToMinutes(
        playlistSongs.sumOf { it.durationMillis }
    )
    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        PlaylistViewHeader(
            playlistName = playlist.playlistName,
            playlistDurationMins = playlistDurationMins,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SongList(
            currentSong = currentSong,
            songList = playlistSongs,
            topEdgePadding = topEdgePadding,
            bottomEdgePadding = bottomEdgePadding,
            playerState = playerState,
            onSongItemClick = onSongItemClick
        )
    }
}

@Preview
@Composable
private fun PlaylistViewScreenPreview() {
    PreviewColumn() {
        PlaylistViewScreen(
            playlist = dummyPlaylist,
            currentSong = null,
            playlistSongs = dummySongList,
            topEdgePadding = 0f,
            bottomEdgePadding = 0f,
            playerState = PlayerState(),
            onSongItemClick = { _, _ -> }
        )
    }
}