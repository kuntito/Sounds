package com.example.sounds.data.models

import com.example.sounds.data.local.song.SongEntity

data class Song(
    val id: String,
    val title: String,
    val artistName: String,
    val albumArtFilePath: String? = null,
)

fun SongEntity.toSong() = Song(
    id = id,
    title = title,
    artistName = artist,
    albumArtFilePath = albumArtFilePath
)

val dummySong = Song(
    id = "0",
    title = "Monica Lewinsky",
    artistName = "SAINt JHN",
)

val dummySongList = listOf(
    Song(
        id = "0",
        title = "Monica Lewinsky",
        artistName = "SAINt JHN",
    ),
    Song(
        id = "1",
        title = "Switched Up",
        artistName = "Nasty C",
    ),
    Song(
        id = "2",
        title = "Storage",
        artistName = "Conor Maynard",
    ),
    Song(
        id = "3",
        title = "Understand",
        artistName = "Omah Lay",
    ),
    Song(
        id = "4",
        title = "Waka Jeje",
        artistName = "BNXN (feat. Majeeed)",
    ),
    Song(
        id = "5",
        title = "Naira Marley",
        artistName = "Zinoleesky",
    ),
    Song(
        id = "6",
        title = "Champion",
        artistName = "Elina",
    ),
    Song(
        id = "7",
        title = "Again",
        artistName = "Sasha Sloan",
    ),
    Song(
        id = "8",
        title = "smiling when i die",
        artistName = "Sasha Sloan",
    ),
    Song(
        id = "9",
        title = "Dealer",
        artistName = "Ayo Maff (feat. FireboyDML)",
    ),
    Song(
        id = "10",
        title = "365 Days",
        artistName = "Tml Vibez",
    ),
    Song(
        id = "11",
        title = "Design",
        artistName = "Olivetheboy",
    ),
    Song(
        id = "12",
        title = "Rara",
        artistName = "Tml Vibez",
    ),
    Song(
        id = "13",
        title = "Fall Back",
        artistName = "Lithe",
    ),
    Song(
        id = "14",
        title = "Can't Breathe",
        artistName = "Llona",
    ),
    Song(
        id = "15",
        title = "23",
        artistName = "Burna Boy",
    ),
    Song(
        id = "16",
        title = "HBP (Remix)",
        artistName = "Llona (feat. Bella Shmurda)",
    ),
    Song(
        id = "17",
        title = "Trees",
        artistName = "Olivetheboy",
    ),
    Song(
        id = "18",
        title = "Worst Luck",
        artistName = "6LACK",
    ),
    Song(
        id = "19",
        title = "Dreams",
        artistName = "NF",
    ),
)