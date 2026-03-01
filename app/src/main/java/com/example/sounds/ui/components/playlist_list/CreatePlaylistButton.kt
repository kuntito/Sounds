package com.example.sounds.ui.components.playlist_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.AppTextButton
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun CreatePlaylistButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    AppTextButton(
        text = "create playlist",
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun CreatePlaylistPreview() {
    PreviewColumn {
        CreatePlaylistButton(
            onClick = {},
        )
    }
}