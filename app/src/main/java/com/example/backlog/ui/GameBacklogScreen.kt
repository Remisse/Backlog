package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.backlog.R
import com.example.backlog.model.database.entity.Game
import com.example.backlog.viewmodel.GameViewModel
import kotlinx.coroutines.launch

@Composable
private fun ItemCardList(games: List<Game>, padding: PaddingValues, onEditClick: (Game) -> Unit,
                         onDeleteClick: (Game) -> Unit) {
    LazyColumn(modifier = Modifier.padding(padding)) {
        items(games) { game ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                topText = {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.h6
                    )
                },
                subText = { Text(text = stringResource(gameStatusToResource(game.status))) },
                hiddenText = { Text(text = game.platform) },
                onEditClick = { onEditClick(game) },
                onDeleteClick = { onDeleteClick(game) }
            )
        }
    }
}

@Composable
private fun BacklogMiniFabs(onOnlineSearchClick: () -> Unit, onCreateClick: () -> Unit) {
    val subButtonHeight = 36.dp
    val subButtonFontSize = 10.sp
    val subButtonIconSize = 16.dp
    val subButtonColor = MaterialTheme.colors.secondaryVariant

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
        modifier = Modifier.height(subButtonHeight),
        backgroundColor = subButtonColor
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
        modifier = Modifier.height(subButtonHeight),
        backgroundColor = subButtonColor
    )
}

@Composable
fun BacklogScreen(onCreateButtonClick: () -> Unit, onOnlineSearchButtonClick: () -> Unit,
                  onEditCardClick: (Game) -> Unit, fabModifier: Modifier,
                  gameViewModel: GameViewModel) {
    val gamesFlow = gameViewModel.backlog

    val gameUidToDelete: MutableState<Int?> = remember { mutableStateOf(null) }

    val failureToast = Toast.makeText(LocalContext.current, stringResource(R.string.delete_failure_toast), Toast.LENGTH_SHORT)
    val resetDeleteState = { gameUidToDelete.value = null }

    if (gameUidToDelete.value != null) {
        DeleteDialog(
            onDismissRequest = resetDeleteState,
            onConfirmDeleteClick = {
                gameViewModel.delete(
                    gameUidToDelete.value!!,
                    onSuccess = resetDeleteState,
                    onFailure = {
                        resetDeleteState()
                        failureToast.show()
                    }
                )
            },
            onCancelClick = resetDeleteState,
            body = R.string.game_delete_dialog_body
        )
    }
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
    ) { padding ->
        gamesFlow.collectAsState(initial = listOf()).value.apply {
            ItemCardList(
                games = this,
                padding = padding,
                onEditClick = onEditCardClick,
                onDeleteClick = { game -> gameUidToDelete.value = game.uid }
            )
        }
    }
}
