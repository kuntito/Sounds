package com.example.sounds.player

import com.example.sounds.data.models.Song

data class PlaybackActions (
    val onPlay: (Song) -> Unit,
    val onPause: () -> Unit,
    val onSeekTo: (Float) -> Unit,
    val onNext: () -> Unit,
    val onPrev: () -> Unit,
    val toggleShuffle: () -> Unit,
    val onSwapSong: (Int, Int) -> Unit,
    val onSongItemClick: (Int, List<Song>) -> Unit,
)

val dummyPlaybackActions = PlaybackActions(
    onPlay = {},
    onPause = {},
    onSeekTo = {},
    onNext = {},
    onPrev = {},
    toggleShuffle = {},
    onSwapSong = { _, _ ->},
    onSongItemClick = { _, _ -> },
)