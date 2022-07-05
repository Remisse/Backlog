package com.example.backlog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backlog.R
import com.example.backlog.database.entity.Game
import com.example.backlog.viewmodel.GameViewModel

@Composable
private fun ItemCardList(cards: List<Game>, padding: PaddingValues) {
    val textModifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

    LazyColumn(modifier = Modifier.padding(padding)) {
        items(cards) { card ->
            ItemCard(
                topText = {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.h6,
                        modifier = textModifier
                    )
                },
                subText = listOf { Text(text = card.status, modifier = textModifier) },
                hiddenText = listOf { Text(text = card.platform, modifier = textModifier) }
            )
        }
    }
}

@Composable
private fun BacklogMiniFabs(onOnlineSearchClick: () -> Unit, onCreateClick: () -> Unit) {
    val subButtonHeight = 36.dp
    val subButtonFontSize = 9.sp
    val subButtonIconSize = 16.dp

    ExtendedFloatingActionButton(
        text = { Text(
            text = stringResource(R.string.backlog_fab_onlinesearch).uppercase(),
            fontSize = subButtonFontSize
        ) },
        onClick = onOnlineSearchClick,
        icon = { Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(subButtonIconSize)
        ) },
        modifier = Modifier.height(subButtonHeight)
    )
    ExtendedFloatingActionButton(
        text = { Text(
            text = stringResource(R.string.backlog_fab_create).uppercase(),
            fontSize = subButtonFontSize
        ) },
        onClick = onCreateClick,
        icon = { Icon(
            imageVector = Icons.Default.Create,
            contentDescription = null,
            modifier = Modifier.size(subButtonIconSize)
        ) },
        modifier = Modifier.height(subButtonHeight)
    )
}

@Composable
fun BacklogScreen(onCreateButtonClick: () -> Unit, onOnlineSearchButtonClick: () -> Unit,
                  fabModifier: Modifier, gameViewModel: GameViewModel) {
    val games = gameViewModel.backlog.observeAsState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = {
            ActionsFab(
                textRes = R.string.backlog_fab_add,
                icon = Icons.Default.Add,
                modifier = fabModifier
            ) {
                BacklogMiniFabs(
                    onOnlineSearchClick = onOnlineSearchButtonClick,
                    onCreateClick = onCreateButtonClick
                )
            }
        }
    ) {
        ItemCardList(cards = games.value.orEmpty(), padding = it)
    }
}
