package com.example.sounds.ui.components.playlist_list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorIsco
import com.example.sounds.ui.theme.tsOrion

@Composable
fun PlaylistListItem(
    modifier: Modifier = Modifier,
    playlistName: String,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = {},
        isRippleBounded = true,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(40.dp)
                .fillMaxWidth()
            ,
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(R.drawable.ic_playlist),
                contentDescription = null,
                tint = colorIsco,
                modifier = Modifier
                    .size(16.dp)
                ,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = playlistName,
                style = tsOrion,
                modifier = Modifier
                    .weight(1f)
                ,
            )
            Spacer(modifier = Modifier.width(16.dp))
            AppIconButton(
                iconRes = R.drawable.ic_more_vert
            ) { }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview
@Composable
private fun PlaylistListItemPreview() {
    PreviewColumn() {
        PlaylistListItem(
            playlistName = "flames",
            onClick = {},
        )
    }
}