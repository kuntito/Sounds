package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AlbumArt
import com.example.sounds.ui.theme.colorTelli
import java.io.File


/**
 * animates album art from mini player to expanded state.
 * part of an expandable sheet for currently playing songs.
 *
 * mini player: small image, left-aligned
 * expanded state: large image, centered horizontally
 *
 * size and position animate based on fractionOfSheetExpanded (0f to 1f).
 *
 * extracted from parent sheet component for separation of concerns.
 */
@Composable
fun ExpandableAlbumArtSP(
    fractionOfSheetExpanded: Float,
    miniPlayerHeight: Int,
    horizontalPadding: Int,
    albumArtFilePath: String?,
    modifier: Modifier = Modifier,
) {
    val minImageSize = 32
    val maxImageSize = 256
    val screenWidth = LocalConfiguration.current.screenWidthDp


    AlbumArt(
        filePath = albumArtFilePath,
        loadSize = maxImageSize,
        modifier = modifier
            .padding(
                // horizontal padding animates from left-aligned to center position.
                // centering achieved by calculating padding needed:
                // `(screenWidth - imageSize) / 2`
                horizontal = lerp(
                    horizontalPadding.dp,
                    ((screenWidth - maxImageSize) / 2).dp,
                    fractionOfSheetExpanded,
                ),
                vertical = lerp(
                    start = ((miniPlayerHeight - minImageSize) / 2).dp,
                    stop = 0.dp,
                    fractionOfSheetExpanded,
                )
            )
            .size(
                lerp(
                    minImageSize.dp,
                    maxImageSize.dp,
                    fractionOfSheetExpanded
                )
            )
    )
}
