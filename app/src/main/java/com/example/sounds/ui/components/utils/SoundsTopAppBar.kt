package com.example.sounds.ui.components.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.models.dummyDropdownOptions
import com.example.sounds.ui.theme.colorKDB
import com.example.sounds.ui.theme.colorTelli
import com.example.sounds.ui.theme.tsOrion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundsTopAppBar(
    modifier: Modifier = Modifier,
    dropdownOptions: List<DropdownMenuOption>,
) {
    val containerColor = colorKDB
    val foregroundColor = colorTelli

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
//            .border(width = 1.dp, color = Color.Yellow)
            .padding(start = 16.dp, end = 8.dp)
        ,
    ) {
        Text(
            text = "Sounds",
            style = tsOrion
                .copy(
                    fontWeight = FontWeight.Bold,
                ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )
        AppDropdownMenu(
            dropdownOptions = dropdownOptions,
        )
    }
}

@Preview
@Composable
private fun SoundsTopAppBarPreview() {
    PreviewColumn() {
        SoundsTopAppBar(
            dropdownOptions = dummyDropdownOptions
        )
    }
}