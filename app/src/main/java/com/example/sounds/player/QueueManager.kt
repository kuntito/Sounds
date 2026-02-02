package com.example.sounds.player

import com.example.sounds.data.models.Song

class QueueManager {
    private var queueOfSongs: List<Song> = emptyList()
    private var currentIndex: Int = 0

    fun setup(
        indexClickedSong: Int,
        songList: List<Song>,
    ) {
        queueOfSongs = songList
        currentIndex = indexClickedSong
    }

    fun next(): Song? {
        if (currentIndex + 1 == queueOfSongs.size) return null

        currentIndex++
        return queueOfSongs[currentIndex]
    }

    fun previous(): Song? {
        if (currentIndex == 0) return null

        currentIndex--
        return queueOfSongs[currentIndex]
    }
}