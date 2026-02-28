package com.example.sounds.ui.components.playlist_add_tracks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.CustomSearchTextField
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.components.utils.rememberCustomTextFieldState
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorTelli

@Composable
fun AddTrackSearchBar(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
) {

    val searchFieldState = rememberCustomTextFieldState(onQueryChange = onQueryChange)
    CustomSearchTextField(
        textFieldState = searchFieldState,
        containerColor = colorAguero,
        cursorColor = colorTelli,
        bookendIconsColor = colorTelli,
        textColor = colorTelli,
        modifier = modifier
        ,
    )

}

@Preview
@Composable
private fun AddTrackSearchBarPreview() {
    PreviewColumn {
        val onQueryChange: (String) -> Unit = { }
        AddTrackSearchBar(
            onQueryChange = onQueryChange,
        )
    }
}