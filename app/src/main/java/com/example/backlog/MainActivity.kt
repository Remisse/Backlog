package com.example.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backlog.model.entities.Game
import com.example.backlog.ui.BottomNavigationBar
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val container = (application as BacklogApplication).appContainer
    private val gameViewModel = container.gameViewModelFactory.create(GameViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val games = gameViewModel.backlog.value ?: emptyList()
        setContent {
            BacklogTheme() {
                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = { SearchBar(value = "", onValueChange = {}) },
                    bottomBar = { BottomNavigationBar(
                        onClickGameList = {},
                        onClickTaskList = { /*TODO*/ },
                        onClickProfile = { /*TODO*/ })
                    }) {
                    ItemCardList(cards = games)
                }
            }
        }
    }
}

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
                    .height(96.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = platform
                )
                Text(
                    text = status
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
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
fun CardCreationFabElement() {
    FloatingActionButton(onClick = { /* TODO */ }) {
        Icon(imageVector = Icons.Default.Create, contentDescription = null)
    }
}

@Composable
fun CardOnlineSearchFabElement() {
    FloatingActionButton(onClick = { /* TODO */ }) {
        Icon(imageVector = Icons.Default.Cloud, contentDescription = null)
    }
}

@Composable
fun CardActionsFab() {
    val isClicked: MutableState<Boolean> = remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isClicked.value, label = "FabClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 45f }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isClicked.value) {
            CardOnlineSearchFabElement()
            CardCreationFabElement()
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

@Preview(showBackground = false)
@Composable
fun GameListActivityPreview() {
    BacklogTheme() {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = { SearchBar(value = "", onValueChange = {}) },
            bottomBar = { BottomNavigationBar(
                            onClickGameList = { /*TODO*/ },
                            onClickTaskList = { /*TODO*/ },
                            onClickProfile = { /*TODO*/ })
                        },
            floatingActionButton = { CardActionsFab() }
        ) {
            ItemCardList(cards = listOf(
                Game(1, "Metal Gear Solid 3: Snake Eater", "PlayStation 2", "Playing", null),
                Game(2, "Super Mario Odyssey", "Switch", "Playing", null)
            ))
        }
    }
}