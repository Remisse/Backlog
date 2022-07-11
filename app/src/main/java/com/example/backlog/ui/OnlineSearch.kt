package com.example.backlog.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
private fun TopSearchBar(searchValue: String, onValueChange: (String) -> Unit, onBackClick: () -> Unit) {
    TopAppBar() {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        SearchBar(
            value = searchValue,
            onValueChange = onValueChange,
            modifier = LookAndFeel.FieldModifier,
            shape = LookAndFeel.FieldShape
        )
    }
}


