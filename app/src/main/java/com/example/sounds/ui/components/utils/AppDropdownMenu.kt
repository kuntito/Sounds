package com.example.sounds.ui.components.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.R
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.models.dummyDropdownOptions
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorTelli

@Composable
fun AppDropdownMenu(
    modifier: Modifier = Modifier,
    dropdownOptions: List<DropdownMenuOption>
) {
    var isMenuVisible by remember {
        mutableStateOf(false)
    }

    val hideMenu: () -> Unit = {
        isMenuVisible = false
    }

    val showMenu: () -> Unit = {
        isMenuVisible = true
    }

    Box(
        modifier = modifier,
    ) {
        AppIconButton(
            color = colorTelli,
            iconRes = R.drawable.ic_more_vert,
            onClick = {
                showMenu()
            }
        )

        DropdownMenu(
            expanded = isMenuVisible,
            containerColor = colorAguero,
            onDismissRequest = hideMenu,
        ) {
           dropdownOptions.forEach { option ->
               DropdownMenuItem(
                   text = {
                       Text(
                           text = option.label
                       )
                   },
                   onClick = {
                       option.onClick()
                       hideMenu()
                   }
               )
           }
        }
    }
}

@Preview
@Composable
private fun DropdownMenuPreview() {
    PreviewColumn() {
        AppDropdownMenu(
            dropdownOptions = dummyDropdownOptions,
        )
    }
}