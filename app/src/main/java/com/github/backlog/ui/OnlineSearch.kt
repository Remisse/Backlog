package com.github.backlog.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import com.github.backlog.model.database.entity.Game
import com.github.backlog.ui.components.LookAndFeel
import com.github.backlog.ui.components.SearchBar
import com.github.backlog.viewmodel.GameViewModel

@Composable
private fun TopSearchBar(searchValue: String, onValueChange: (String) -> Unit,
                         onBackClick: () -> Unit, onSearchSubmit: () -> Unit) {
    TopAppBar {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Row {
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
            Row {
                IconButton(onClick = onSearchSubmit) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun ResultList(results: List<Game>) {
    LazyColumn() {
        items(results) { item ->
            OutlinedButton(
                onClick = { /*TODO*/ }
            ) {
                Text(text = "${item.title} (${item.platform})")
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OnlineSearchScreen(onBackClick: () -> Unit, onGameAdd: () -> Unit, gameViewModel: GameViewModel) {
    var searchQuery: String by remember { mutableStateOf("") }

    Scaffold(
        topBar = { 
            TopSearchBar(
                searchValue = searchQuery, 
                onValueChange = { searchQuery = it }, 
                onBackClick = onBackClick,
                onSearchSubmit = {

                }
            ) 
        }
    ) {
        
    }
}
