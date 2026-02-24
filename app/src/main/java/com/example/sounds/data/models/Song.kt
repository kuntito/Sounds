package com.example.sounds.data.models

import com.example.sounds.data.local.song.SongEntity

data class Song(
    val id: String,
    val title: String,
    val artistName: String,
    val durationMillis: Int,
    val albumArtFilePath: String? = null,
    val songFilePath: String,
)

fun SongEntity.toSong() = Song(
    id = id,
    title = title,
    artistName = artist,
    durationMillis = durationMillis,
    albumArtFilePath = albumArtFilePath,
    songFilePath = songFilePath,
)

val dummySong = Song(
    id = "0",
    title = "Monica Lewinsky",
    artistName = "SAINt JHN",
    durationMillis = 150000,
    songFilePath = "",
)

val dummySongList = listOf(
    Song(
        id = "0",
        title = "Monica Lewinsky",
        artistName = "SAINt JHN",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "1",
        title = "Switched Up",
        artistName = "Nasty C",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "2",
        title = "Storage",
        artistName = "Conor Maynard",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "3",
        title = "Understand",
        artistName = "Omah Lay",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "4",
        title = "Waka Jeje",
        artistName = "BNXN (feat. Majeeed)",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "5",
        title = "Naira Marley",
        artistName = "Zinoleesky",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "6",
        title = "Champion",
        artistName = "Elina",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "7",
        title = "Again",
        artistName = "Sasha Sloan",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "8",
        title = "smiling when i die",
        artistName = "Sasha Sloan",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "9",
        title = "Dealer",
        artistName = "Ayo Maff (feat. FireboyDML)",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "10",
        title = "365 Days",
        artistName = "Tml Vibez",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "11",
        title = "Design",
        artistName = "Olivetheboy",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "12",
        title = "Rara",
        artistName = "Tml Vibez",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "13",
        title = "Fall Back",
        artistName = "Lithe",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "14",
        title = "Can't Breathe",
        artistName = "Llona",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "15",
        title = "23",
        artistName = "Burna Boy",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "16",
        title = "HBP (Remix)",
        artistName = "Llona (feat. Bella Shmurda)",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "17",
        title = "Trees",
        artistName = "Olivetheboy",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "18",
        title = "Worst Luck",
        artistName = "6LACK",
        durationMillis = 150000,
        songFilePath = "",
    ),
    Song(
        id = "19",
        title = "Dreams",
        artistName = "NF",
        durationMillis = 150000,
        songFilePath = "",
    ),
)