package com.example.sounds.ui.components.playlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.tsOrion

@Composable
fun CreatePlaylistButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    ClickableSurface (
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
        ,
    ) {
        Text(
            "create playlist",
            style = tsOrion,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 4.dp,
                )
            ,
        )
    }
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