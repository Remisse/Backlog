package com.example.backlog.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import com.example.backlog.model.database.entity.Game
import com.example.backlog.ui.common.LookAndFeel
import com.example.backlog.ui.common.SearchBar
import com.example.backlog.viewmodel.GameViewModel

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

@Composable
private fun ResultList(results: List<Game>) {
    LazyColumn() {
        items(results) { item ->
            OutlinedButton(onClick = { /*TODO*/ }) {
                
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
                onBackClick = onBackClick
            ) 
        }
    ) {
        
    }
}
