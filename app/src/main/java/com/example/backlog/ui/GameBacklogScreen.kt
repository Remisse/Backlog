package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backlog.R
import com.example.backlog.model.GameStatus
import com.example.backlog.model.database.entity.Game
import com.example.backlog.viewmodel.GameViewModel
import java.time.LocalDate
import java.util.*

@Composable
private fun ItemCardList(games: List<Game>, onEditClick: (Int) -> Unit, onDeleteClick: (Game) -> Unit,
                         onChangeStatusClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    LazyColumn(modifier = modifier) {
        items(games) { game ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                exposedText = {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(gameStatusToResource(game.status)).uppercase(),
                            color = gameStatusToColor(game.status),
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = game.title,
                            style = MaterialTheme.typography.subtitle2
                        )
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    }
                },
                hiddenText = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        CardSubtitleTextIcon(text = game.platform, imageVector = Icons.Outlined.VideogameAsset)
                        game.releaseDate?.let {
                            CardSubtitleTextIcon(
                                text = format.format(LocalDate.ofEpochDay(it)),
                                imageVector = Icons.Default.CalendarToday
                            )
                        }
                        game.genre.takeIf { it != "" }
                            ?.let { CardSubtitleTextIcon(text = it, imageVector = Icons.Outlined.Category) }
                        game.developer.takeIf { it != "" }
                            ?.let { CardSubtitleTextIcon(text = it, imageVector = Icons.Outlined.Code) }
                        game.publisher.takeIf { it != "" }
                            ?.let { CardSubtitleTextIcon(text = it, imageVector = Icons.Outlined.GroupWork) }
                    }
                },
                onChangeStatusClick = { onChangeStatusClick(game.uid) },
                onEditClick = { onEditClick(game.uid) },
                onDeleteClick = { onDeleteClick(game) }
            )
        }
    }
}

@Composable
private fun BacklogMiniFabs(onOnlineSearchClick: () -> Unit, onCreateClick: () -> Unit) {
    val subButtonHeight = 36.dp
    val subButtonFontSize = (9.5).sp
    val subButtonIconSize = 16.dp
    val subButtonColor = MaterialTheme.colors.secondaryVariant

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
    ExtendedFloatingActionButton(
        text = { Text(
            text = stringResource(R.string.backlog_fab_onlinesearch).uppercase(),
            fontSize = subButtonFontSize
        ) },
        onClick = onOnlineSearchClick,
        icon = { Icon(
            imageVector = Icons.Default.Public,
            contentDescription = null,
            modifier = Modifier.size(subButtonIconSize)
        ) },
        modifier = Modifier.height(subButtonHeight),
        backgroundColor = subButtonColor
    )
}

@Composable
fun BacklogFab(modifier: Modifier = Modifier, onOnlineSearchButtonClick: () -> Unit,
               onCreateButtonClick: () -> Unit) {
    ActionsFab(
        textRes = R.string.backlog_fab_add,
        icon = Icons.Default.Add,
        modifier = modifier
    ) {
        BacklogMiniFabs(
            onOnlineSearchClick = onOnlineSearchButtonClick,
            onCreateClick = onCreateButtonClick
        )
    }
}

@Composable
fun BacklogScreen(onEditCardClick: (Int) -> Unit, gameViewModel: GameViewModel) {
    val gameUidStatusUpdate: MutableState<Int?> = remember { mutableStateOf(null) }
    val resetChangeState = { gameUidStatusUpdate.value = null }

    val gameUidToDelete: MutableState<Int?> = remember { mutableStateOf(null) }
    val resetDelete = { gameUidToDelete.value = null }
    val failureToast = Toast.makeText(LocalContext.current, stringResource(R.string.delete_failure_toast), Toast.LENGTH_SHORT)

    if (gameUidToDelete.value != null) {
        DeleteDialog(
            onDismissRequest = resetDelete,
            onConfirmDeleteClick = {
                gameViewModel.delete(
                    gameUidToDelete.value!!,
                    onSuccess = resetDelete,
                    onFailure = {
                        resetDelete()
                        failureToast.show()
                    }
                )
            },
            onCancelClick = resetDelete,
            body = R.string.game_delete_dialog_body
        )
    }
    if (gameUidStatusUpdate.value != null) {
        Surface(shape = LookAndFeel.DialogSurfaceShape, modifier = Modifier.padding(16.dp)) {
            StatusMenu<GameStatus>(
                expanded = gameUidStatusUpdate.value != null,
                onSelect = {
                    gameViewModel.setStatus(gameUidStatusUpdate.value!!, it)
                    resetChangeState()
                },
                onDismissRequest = resetChangeState,
                toColor = { gameStatusToColor(it) },
                toResource = { gameStatusToResource(it) }
            )
        }
    }
    gameViewModel.backlog.collectAsState(initial = listOf()).value.apply {
        if (this.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.backlog_empty),
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            ItemCardList(
                games = this,
                onChangeStatusClick = { gameUidStatusUpdate.value = it },
                onEditClick = onEditCardClick,
                onDeleteClick = { gameUidToDelete.value = it.uid }
            )
        }
    }
}
