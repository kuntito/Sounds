package com.example.sounds.ui.components.playlist_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.ui.components.utils.AppDropdownMenu
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorIsco
import com.example.sounds.ui.theme.tsMonoMini
import com.example.sounds.ui.theme.tsOrion

const val iconSize = 24
@Composable
fun PlaylistViewHeader(
    modifier: Modifier = Modifier,
    playlistName: String,
    playlistHasSongs: Boolean,
    playlistDurationMins: Int,
    onNavBackClick: () -> Unit,
    onAddTracksExistingPlaylist: () -> Unit,
    playlistViewMenuOptions: List<DropdownMenuOption>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(start = 6.dp)
            .height(64.dp)
            .fillMaxWidth()
        ,
    ) {
        SpacedRow(
            isSecondRow = false,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_left_chevron,
                size = iconSize,
            ) { onNavBackClick() }
            Text(
                text = playlistName,
                style = tsOrion,
                modifier = Modifier
                    .weight(1f)
                ,
            )
            Row() {
                AnimatedVisibility(
                    visible = playlistHasSongs
                ) {
                    AppIconButton(
                        iconRes = R.drawable.ic_plus,
                        size = iconSize,
                        modifier = Modifier.padding(end = 24.dp)
                    ) {
                        onAddTracksExistingPlaylist()
                    }
                }
                AppDropdownMenu(dropdownOptions = playlistViewMenuOptions)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        SpacedRow(
            isSecondRow = true,
        ) {
            Text(
                text = "$playlistDurationMins mins",
                style = tsMonoMini
                    .copy(
                        color = colorIsco,
                    ),
            )
        }
    }
}

/**
 * PlaylistViewHeader has two rows.
 *
 * the first has an icon then other components.
 * the second has no icon then other components.
 *
 * i want the second item in both rows to start at the same position.
 * so i hardcoded space for the icon in the second row and used this
 * composable to hardcode the horizontal spacing between the first
 * item and the rest.
 */
@Composable
private fun SpacedRow(
    modifier: Modifier = Modifier,
    spacing: Int = 16,
    isSecondRow: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if(isSecondRow) {
            Spacer(modifier = Modifier.width(iconSize.dp))
        }
        content()
    }
}

@Preview
@Composable
private fun PlaylistViewHeaderPreview() {
    PreviewColumn {
        PlaylistViewHeader(
            playlistName = "Faraway",
            playlistDurationMins = 20,
            playlistHasSongs = true,
            onNavBackClick = {},
            onAddTracksExistingPlaylist = {},
            playlistViewMenuOptions = emptyList(),
        )
    }
}