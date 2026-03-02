package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.MINI_PLAYER_HEIGHT
import com.example.sounds.data.local.playlist.NoSongsInPlaylistPrompt
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.models.PlaylistWithSongs
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummyPlaylist
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.components.playlist_list.PlaylistSongList
import com.example.sounds.ui.components.playlist_view.PlaylistViewHeader
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.utils.millisToMinutes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

// TODO start here, mini player should be visible on this screen
@Composable
fun PlaylistViewScreenRoot(
    playlistId: Long,
    songViewModel: SongViewModel,
    onBackNav: () -> Unit,
    goToPlaylistAddTracksScreen: () -> Unit,
) {
    val playlistWithSongs by remember(playlistId) {
        songViewModel.getPlaylistWithSongs(playlistId)
    }.collectAsState(null)
    val onSongItemClick = songViewModel.playbackActions.onSongItemClick
    val removeSongFromPlaylist = songViewModel::removeSongFromPlaylist
    val onRemoveSongSuccess = songViewModel.removeSongResult

    playlistWithSongs?.let {
        PlaylistViewScreen(
            playlistWithSongs = it,
            removeSongFromPlaylist = removeSongFromPlaylist,
            onSongItemClick = onSongItemClick,
            onRemoveSongSuccess = onRemoveSongSuccess,
            onBackNav = onBackNav,
            onAddTracksExistingPlaylistClick = {
                goToPlaylistAddTracksScreen()
                songViewModel.onAddTracksExistingPlaylistClick(it.playlist)
            },
        )
    }
}

@Composable
fun PlaylistViewScreen(
    modifier: Modifier = Modifier,
    playlistWithSongs: PlaylistWithSongs,
    onSongItemClick: (Int, List<Song>) -> Unit,
    removeSongFromPlaylist: (Song, Long) -> Unit,
    onRemoveSongSuccess: Flow<String>,
    onBackNav: () -> Unit,
    onAddTracksExistingPlaylistClick: () -> Unit,
) {
    val playlistDurationMins = millisToMinutes(
        playlistWithSongs.playlistSongs.sumOf { it.durationMillis }
    )

    val getPlaylistSongMenuOptions: (Song) -> List<DropdownMenuOption> = { song ->
        listOf<DropdownMenuOption>(
            DropdownMenuOption(
                label = "remove from playlist",
                onClick = {
                    removeSongFromPlaylist(song, playlistWithSongs.playlist.id)
                }
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        PlaylistViewHeader(
            playlistName = playlistWithSongs.playlist.playlistName,
            playlistDurationMins = playlistDurationMins,
            playlistHasSongs = playlistWithSongs.playlistSongs.isNotEmpty(),
            onBackNav = onBackNav,
            onAddTracksExistingPlaylist = onAddTracksExistingPlaylistClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (playlistWithSongs.playlistSongs.isEmpty()) {
            NoSongsInPlaylistPrompt(
                onAddSongExistingPlaylistClick = onAddTracksExistingPlaylistClick
            )
        } else {
            PlaylistSongList(
                playlistSongs = playlistWithSongs.playlistSongs,
                topEdgePadding = 0f,
                bottomEdgePadding = MINI_PLAYER_HEIGHT.toFloat(),
                getPlaylistSongMenuOptions = getPlaylistSongMenuOptions,
                onSongItemClick = onSongItemClick,
                onRemoveSongSuccess = onRemoveSongSuccess,
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistViewScreenPreview() {
    PreviewColumn() {
        val playlistWithSongs = PlaylistWithSongs(
            playlist = dummyPlaylist,
            playlistSongs = emptyList(),
        )

        PlaylistViewScreen(
            playlistWithSongs = playlistWithSongs,
            removeSongFromPlaylist = { _, _ -> },
            onSongItemClick = { _, _ -> },
            onRemoveSongSuccess = MutableSharedFlow(),
            onBackNav = { },
            onAddTracksExistingPlaylistClick = { },
        )
    }
}