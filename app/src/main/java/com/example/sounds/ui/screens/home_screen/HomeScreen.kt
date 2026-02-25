package com.example.sounds.ui.screens.home_screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.models.Playlist
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummyPlaylistList
import com.example.sounds.data.models.dummySongList
import com.example.sounds.data.repository.SyncState
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.PlaybackRepeatModes
import com.example.sounds.player.PlayerState
import com.example.sounds.player.dummyPlaybackActions
import com.example.sounds.ui.HomeScreenTabs
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.AppSnackBar
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.components.utils.RowPagerWithTabs
import com.example.sounds.ui.components.utils.SoundsTopAppBar
import com.example.sounds.ui.screens.home_screen.fragments.PlaylistListFragment
import com.example.sounds.ui.screens.home_screen.fragments.TrackListFragment

// TODO can variables be cleaned up?
@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel,
    goToPlaylistAddTracks: () -> Unit,
) {
    val trackList by songViewModel.songs.collectAsState()
    val playerState by songViewModel.playerState.collectAsState()
    val songQueue by songViewModel.songQueue.collectAsState()
    val currentSong by songViewModel.currentSong.collectAsState()
    val previousSong by songViewModel.previousSong.collectAsState()
    val nextSong by songViewModel.nextSong.collectAsState()
    val isShuffled by songViewModel.isShuffled.collectAsState()
    val currentTrackNumber by songViewModel.currentTrackNumber.collectAsState()
    val playbackRepeatMode by songViewModel.playbackRepeatMode.collectAsState()
    val syncState by songViewModel.syncState.collectAsState()
    val songSync = songViewModel::sync
    val playbackActions = songViewModel.playbackActions
    val playlists= songViewModel.playlists
    val onPlaylistClick = songViewModel::onPlaylistClick
    val onCreatePlaylistClick = {
        goToPlaylistAddTracks()
        songViewModel.onCreatePlaylistClick()
    }

    HomeScreen(
        songSync = songSync,
        trackList = trackList,
        playerState = playerState,
        playbackActions = playbackActions,
        currentSong = currentSong,
        previousSong = previousSong,
        nextSong = nextSong,
        playlists = playlists,
        onPlaylistClick = onPlaylistClick,
        onCreatePlaylistClick = onCreatePlaylistClick,
        songQueue = songQueue,
        isShuffled = isShuffled,
        playbackRepeatMode = playbackRepeatMode,
        currentTrackNumber = currentTrackNumber,
        syncState = syncState,
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    songSync: () -> Unit,
    trackList: List<Song>,
    playerState: PlayerState,
    playbackActions: PlaybackActions,
    currentSong: Song?,
    previousSong: Song?,
    nextSong: Song?,
    playlists: List<Playlist>,
    onPlaylistClick: (Long) -> Unit,
    onCreatePlaylistClick: () -> Unit,
    songQueue: List<Song>,
    isShuffled: Boolean,
    playbackRepeatMode: PlaybackRepeatModes,
    currentTrackNumber: Int,
    syncState: SyncState,
) {

    val topBarMenuOptions = listOf<DropdownMenuOption>(
        DropdownMenuOption(
            label = "sync",
            onClick = {
                songSync()
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
                    is HomeScreenTabs.TrackList-> TrackListFragment(
                        trackList = trackList,
                        playerState = playerState,
                        playbackActions = playbackActions,
                        currentSong = currentSong,
                        miniPlayerHeight = miniPlayerHeight,
                    )
                    is HomeScreenTabs.Playlists -> PlaylistListFragment(
                        playlists = playlists,
                        miniPlayerHeight = miniPlayerHeight,
                        onPlaylistClick = onPlaylistClick,
                        onCreatePlaylistClick = onCreatePlaylistClick,
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
                playbackActions = playbackActions,
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

@Preview
@Composable
private fun HomeScreenPreview() {
    PreviewColumn {
        val trackList = dummySongList
        val playerState = PlayerState()
        val songQueue = dummySongList
        val currentSong = dummySongList[1]
        val previousSong = dummySongList[0]
        val nextSong = dummySongList[2]
        val isShuffled = false
        val currentTrackNumber = 1
        val playbackRepeatMode = PlaybackRepeatModes.RepeatOne
        val syncState = SyncState.Idle
        val songSync = {}
        val playbackActions = dummyPlaybackActions
        val playlists = dummyPlaylistList
        val onPlaylistClick: (Long) -> Unit = {}
        val onCreatePlaylistClick = {}

        HomeScreen(
            songSync = songSync,
            trackList = trackList,
            playerState = playerState,
            playbackActions = playbackActions,
            currentSong = currentSong,
            previousSong = previousSong,
            nextSong = nextSong,
            playlists = playlists,
            onPlaylistClick = onPlaylistClick,
            onCreatePlaylistClick = onCreatePlaylistClick,
            songQueue = songQueue,
            isShuffled = isShuffled,
            playbackRepeatMode = playbackRepeatMode,
            currentTrackNumber = currentTrackNumber,
            syncState = syncState,
        )
    }
}