package com.example.backlog.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.backlog.AppContainer
import com.example.backlog.Screen
import com.example.backlog.database.entities.Game
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.viewmodel.GameViewModel

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
        label = {
            Row() {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Text("Search")
            }
        }
    )
}

@Composable
fun ItemCard(
    title: String,
    platform: String,
    status: String
) {
    val isClicked = remember { mutableStateOf(false) }
    val buttonIcon = if (!isClicked.value) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp
    val textModifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    modifier = textModifier
                )
                Text(text = status, modifier = textModifier)
                if (isClicked.value) {
                    Text(text = platform, modifier = textModifier)
                }
            }
            IconButton(
                onClick = { isClicked.value = !isClicked.value }
            ) {
                Icon(imageVector = buttonIcon, contentDescription = null)
            }
        }
    }
}

@Composable
fun ItemCardList(cards: List<Game>) {
    LazyColumn() {
        items(cards) { card ->
            ItemCard(title = card.title, platform = card.platform, status = card.status)
        }
    }
}

@Composable
fun CardActionsFab(onCreateClick: () -> Unit, onOnlineSearchClick: () -> Unit) {
    val isClicked: MutableState<Boolean> = remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isClicked.value, label = "FabClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 45f }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isClicked.value) {
            FloatingActionButton(onClick = onOnlineSearchClick) {
                Icon(imageVector = Icons.Default.Cloud, contentDescription = null)
            }
            FloatingActionButton(onClick = onCreateClick) {
                Icon(imageVector = Icons.Default.Create, contentDescription = null)
            }
        }
        FloatingActionButton(onClick = { isClicked.value = !isClicked.value }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotation.value)
            )
        }
    }
}

@Composable
fun BacklogScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val games = gameViewModel.backlog.observeAsState()
    println(games)

    BacklogTheme() {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { SearchBar(value = "", onValueChange = {}) },
            bottomBar = { BottomNavigationBar(navController) },
            floatingActionButton = {
                CardActionsFab(
                    onCreateClick = { navController.navigate(Screen.GameCreation.route) },
                    onOnlineSearchClick = { /* TODO */ }
                )
            }
        ) {
            ItemCardList(cards = games.value.orEmpty())
        }
    }
}
