package com.example.sounds.ui.components.song_playing


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sounds.ui.components.utils.AlbumArtFP
import com.example.sounds.ui.components.utils.Center
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.fontColor

/**
 * Displays album art for the current song and permits swiping to the next or previous song.
 *
 * Under the hood, it's a three-page horizontal pager. The current song's album art sits on the
 * middle page, with the previous and next album arts placed on either side. The component receives
 * all three as file paths (AAFP) as state variables from the caller.
 *
 * To illustrate, let's consider when the user swipes to the next song,
 * the page turns to page 2 and `onSwipeNextSong` fires.
 * This triggers a state update for all three variables:
 *   `truePrevAAFP` takes the value of `currentAAFP`
 *   `currentAAFP` takes the value of `trueNextAAFP`, and
 *   `trueNextAAFP` takes the value of whatever follows.
 *
 * However, when these variables are passed to the component
 * it doesn't update all three at once.
 *
 * Before the swipe,
 * it caches `truePrevAAFP` and `trueNextAAFP` into `cachedPrevAAFP` and `cachedNextAAFP`,
 * and only updates `currentAAFP` immediately.
 *
 * Now we have five variables:
 *   the state updates:
 *     `truePrevAAFP`
 *     `currentAAFP`
 *     `trueNextAAFP`
 *
 *   the caches:
 *     `cachedPrevAAFP`
 *     `cachedNextAAFP`
 *
 *   and `currentAAFP` has the same value as `cachedNextAAFP`
 *
 * At this point the pager is still on page 2 from the swipe, and its pages are
 * `cachedPrevAAFP | currentAAFP (same as cachedNextAAFP) | cachedNextAAFP` (current page, page 2).
 *
 * now, we silently scroll from page 2 back to page 1 — to the user, nothing's happened.
 *
 * Only then do we update `cachedNextAAFP` and `cachedPrevAAFP` to
 * reflect `trueNextAAFP` and `truePrevAAFP`.
 *
 * now the pager is
 * `truePrevAAFP | currentAAFP | trueNextAAFP`
 *
 * This creates the illusion of continuously swiping through songs without breaking the UI.
 *
 * Swiping to the previous song follows the same procedure in reverse:
 *   swipe to page 0
 *   state updates `currentAAFP` to match `cachedPrevAAFP`,
 *   silently scroll from page 0 back to page 1, then
 *   refresh the caches.
 *
 * For edge cases where `truePrevAAFP` or `trueNextAAFP` is null, the component
 * animates back to the middle page — it displays as a bounce-back.
 *
 * @param modifier Modifier for the pager container.
 * @param currentAAFP File path to the current song's album art, or null if none.
 * @param truePrevAAFP File path to the previous song's album art, or null if none.
 * @param trueNextAAFP File path to the next song's album art, or null if none.
 * @param onSwipeNextSong Called when the user swipes to the next song.
 * @param onSwipePrevSong Called when the user swipes to the previous song.
 * @param onSwiping Called with `true` while a swipe is in progress, `false` when settled.
 * @param maxImageSize Maximum size in pixels to load the album art at.
 * @param isSwipeEnabled Whether swiping between songs is enabled.
 */
@Composable
fun SwipeableAlbumArt(
    modifier: Modifier = Modifier,
    currentAAFP: String?,
    truePrevAAFP: String?,
    trueNextAAFP: String?,
    onSwipeNextSong: () -> Unit,
    onSwipePrevSong: () -> Unit,
    onSwiping: (Boolean) -> Unit = {},
    maxImageSize: Int,
    isSwipeEnabled: Boolean,
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 },
    )


    var cachedNextAAFP by remember { mutableStateOf(trueNextAAFP) }
    var cachedPrevAAFP by remember { mutableStateOf(truePrevAAFP) }

    LaunchedEffect(currentAAFP) {
        if (pagerState.currentPage != 1) {
            pagerState.scrollToPage(1)
        }
        cachedNextAAFP = trueNextAAFP
        cachedPrevAAFP = truePrevAAFP
    }

    LaunchedEffect(truePrevAAFP, trueNextAAFP) {
        if (pagerState.currentPage == 1) {
            cachedPrevAAFP = truePrevAAFP
            cachedNextAAFP = trueNextAAFP
        }
    }

    // TODO should be false, if at first or last song..
    //  the pager has three pages, and would always consider scroll in progress
    //  even when prev and next are both null
    val isSwiping by remember {
        derivedStateOf { pagerState.isScrollInProgress }
    }

    LaunchedEffect(isSwiping) {
        onSwiping(isSwiping)
    }

    LaunchedEffect(pagerState.settledPage) {
        when (pagerState.settledPage) {
            0 -> {
                if (truePrevAAFP == null) {
                    pagerState.animateScrollToPage(1)
                } else {
                    onSwipePrevSong()
                }
            }
            2 -> {
                if (trueNextAAFP == null) {
                    pagerState.animateScrollToPage(1)
                } else {
                    onSwipeNextSong()
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = isSwipeEnabled,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
        ,
    ) { page ->
        val albumArtFilePath = when (page) {
            0 -> cachedPrevAAFP
            1 -> currentAAFP
            2 -> cachedNextAAFP
            else -> null
        }
        if (albumArtFilePath != null) {
            Center {
                AlbumArtFP(
                    filePath = albumArtFilePath,
                    loadSize = maxImageSize,
                    modifier = Modifier
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SwipeableAlbumArtPreview() {
    PreviewColumn {
        // the images don't get displayed, since previews can't access local files.
        val aafps: List<String> = listOf(
            "/storage/emulated/0/Android/data/com.example.sounds/files/Pictures/sample_album_art_00.jpg",
            "/storage/emulated/0/Android/data/com.example.sounds/files/Pictures/sample_album_art_01.jpg",
            "/storage/emulated/0/Android/data/com.example.sounds/files/Pictures/sample_album_art_02.jpg",
        )

        var currentIndex by remember { mutableIntStateOf(1) }
        val currentAAFP = aafps.getOrNull(currentIndex)
        val prevAAFP = aafps.getOrNull(currentIndex-1)
        val nextAAFP = aafps.getOrNull(currentIndex+1)

        val maxImageSize = 256
        val isSwipeEnabled = true

        val onSwipeNextSong = {
            if (currentIndex < aafps.lastIndex) {
                currentIndex++
            }
        }

        val onSwipePrevSong = {
            if (currentIndex >= 0) {
                currentIndex--
            }
        }

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center,
        ) {
            SwipeableAlbumArt(
                currentAAFP = currentAAFP,
                truePrevAAFP = prevAAFP,
                trueNextAAFP = nextAAFP,
                onSwipeNextSong = onSwipeNextSong,
                onSwipePrevSong = onSwipePrevSong,
                maxImageSize = maxImageSize,
                isSwipeEnabled = isSwipeEnabled,
                modifier = Modifier
                    .height(300.dp)
            )
            Text(
                text = "page ${currentIndex}",
                fontSize = 48.sp,
                color = Color.Yellow,
            )
        }
    }
}