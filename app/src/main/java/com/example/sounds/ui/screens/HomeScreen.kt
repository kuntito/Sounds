package com.example.sounds.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.repository.SyncState
import com.example.sounds.ui.HomeScreenTabs
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.AppSnackBar
import com.example.sounds.ui.components.utils.RowPagerWithTabs
import com.example.sounds.ui.components.utils.SoundsTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel,
) {
    val songs by songViewModel.songs.collectAsState()
    val playerState by songViewModel.playerState.collectAsState()
    val songQueue by songViewModel.songQueue.collectAsState()
    val currentSong by songViewModel.currentSong.collectAsState()
    val previousSong by songViewModel.previousSong.collectAsState()
    val nextSong by songViewModel.nextSong.collectAsState()
    val isShuffled by songViewModel.isShuffled.collectAsState()
    val currentTrackNumber by songViewModel.currentTrackNumber.collectAsState()
    val playbackRepeatMode by songViewModel.playbackRepeatMode.collectAsState()
    val syncState by songViewModel.syncState.collectAsState()

    val topBarMenuOptions = listOf<DropdownMenuOption>(
        DropdownMenuOption(
            label = "sync",
            onClick = {
                songViewModel.sync()
            },
        )
    )
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val miniPlayerHeight = 48
        Scaffold(
            topBar = {
                SoundsTopAppBar(
                    dropdownOptions = topBarMenuOptions,
                )
            },
            modifier = Modifier
                .fillMaxSize()
            ,
        ) { innerPadding ->
            RowPagerWithTabs(
                tabs = HomeScreenTabs
                    .allTabs
                    .map { it.title },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                ,
            ) { page ->
                when (HomeScreenTabs.allTabs[page]) {
                    is HomeScreenTabs.TrackList-> TrackListScreen(
                        songs = songs,
                        playerState = playerState,
                        playbackActions = songViewModel.playbackActions,
                        currentSong = currentSong,
                        miniPlayerHeight = miniPlayerHeight,
                    )
                    is HomeScreenTabs.Playlists -> PlaylistListScreen(
                        playlists = songViewModel.playlists,
                        miniPlayerHeight = miniPlayerHeight,
                        onPlaylistClick = songViewModel::onPlaylistClick,
                        onCreatePlaylistClick = songViewModel::onCreatePlaylistClick,
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = currentSong != null,
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            SongPlayingSheet(
                miniPlayerHeight = miniPlayerHeight,
                playerState = playerState,
                playbackActions = songViewModel.playbackActions,
                songQueue = songQueue,
                isShuffled = isShuffled,
                playbackRepeatMode = playbackRepeatMode,
                currentSong = currentSong,
                prevSongAAFP = previousSong?.albumArtFilePath,
                nextSongAAFP = nextSong?.albumArtFilePath,
                currentTrackNumber = currentTrackNumber,
            )
        }
        AnimatedVisibility(
            visible = syncState is SyncState.Syncing ||
                    syncState is SyncState.Done ||
                    syncState is SyncState.Error,
            modifier = Modifier
                .padding(top = 128.dp) // experimentally selected
                .align(
                    Alignment.TopCenter
                ),
        ) {
            AppSnackBar(
                message = when(syncState) {
                    is SyncState.Syncing -> "syncing.."
                    is SyncState.Done -> "done"
                    is SyncState.Error -> "sync failed"
                    else -> "middle"
                },
            )
        }
    }
}