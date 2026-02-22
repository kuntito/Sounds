package com.example.sounds.data.models

data class DropdownMenuOption(
    val label: String,
    val onClick: () -> Unit,
)

val sampleDropdownOption: DropdownMenuOption = DropdownMenuOption(
    label = "delete",
    onClick = {},
)

val dummyDropdownOptions = listOf(
    DropdownMenuOption(
        label = "create",
        onClick = {},
    ),
    DropdownMenuOption(
        label = "read",
        onClick = {},
    ),
    DropdownMenuOption(
        label = "update",
        onClick = {},
    ),
    DropdownMenuOption(
        label = "delete",
        onClick = {},
    ),
)