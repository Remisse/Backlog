package com.github.backlog.ui.screen.content.main

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.backlog.R
import com.github.backlog.Section
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.entity.Game
import com.github.backlog.ui.state.filter.FilterState
import com.github.backlog.util.AppContainer
import com.github.backlog.ui.components.*
import com.github.backlog.viewmodel.GameViewModel
import java.time.LocalDate
import java.util.*

class LibraryScreen(private val onEditCardButtonClick: (Int) -> Unit,
                    private val onOnlineSearchButtonClick: () -> Unit,
                    private val onCreateButtonClick: () -> Unit,
                    appContainer: AppContainer
) : MainScreen(appContainer) {

    override val section: Section = Section.Library

    @Composable
    override fun Content(arguments: Bundle?) {
        BacklogScreen(onEditCardClick = onEditCardButtonClick, gameViewModel = viewModelContainer.gameViewModel)
    }

    @Composable
    override fun Fab() {
        BacklogFab(onOnlineSearchButtonClick = onOnlineSearchButtonClick, onCreateButtonClick = onCreateButtonClick)
    }
    
    @Composable
    override fun TopBarExtraButtons() {
        BacklogTopBarExtraButtons(shouldShowFilters = viewModelContainer.gameViewModel.filterState.shouldShowFilters)
    }
}

@Composable
private fun ItemCardList(games: List<Game>, onEditClick: (Int) -> Unit, onDeleteClick: (Game) -> Unit,
                         onChangeStatusClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
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
private fun MiniFab(text: String, onClick: () -> Unit, imageVector: ImageVector,
                    contentDescription: String?) {
    ExtendedFloatingActionButton(
        text = { Text(text = text, fontSize = 9.sp) },
        onClick = onClick,
        icon = { Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp)
        ) },
        modifier = Modifier.height(36.dp),
        backgroundColor = MaterialTheme.colors.secondaryVariant
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
        MiniFab(
            text = stringResource(R.string.backlog_fab_create).uppercase(),
            onClick = onCreateButtonClick,
            imageVector = Icons.Default.Create,
            contentDescription = null
        )
        MiniFab(
            text = stringResource(R.string.backlog_fab_onlinesearch).uppercase(),
            onClick = onOnlineSearchButtonClick,
            imageVector = Icons.Default.Public,
            contentDescription = null
        )
    }
}

@Composable
private fun FilterDialog(state: FilterState) {
    Dialog(
        onDismissRequest = { state.shouldShowFilters.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.filters_heading), style = MaterialTheme.typography.h6)
                Text(text = stringResource(R.string.insert_status_label), style = MaterialTheme.typography.subtitle1)
                state.statusFilters.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = it.value,
                            onCheckedChange = { newValue ->
                                it.value = newValue
                            }
                        )
                        Text(stringResource(it.nameResId))
                    }
                }
            }
        }
    }
}

@Composable
fun BacklogTopBarExtraButtons(shouldShowFilters: MutableState<Boolean>) {
    IconButton(onClick = { shouldShowFilters.value = true }) {
        Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
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
    if (gameViewModel.filterState.shouldShowFilters.value) {
        FilterDialog(gameViewModel.filterState)
    }

    gameViewModel.backlog
        .collectAsState(initial = listOf()).value
        .filter { gameViewModel.filterState.testAll(it) }
        .apply {
            println("ciaooo")
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
