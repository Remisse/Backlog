package com.github.backlog.ui.screen.main.library

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.*
import com.github.backlog.ui.state.filter.GameFilterState
import com.github.backlog.utils.localDateFromEpochSecond
import com.github.backlog.utils.now
import com.github.backlog.viewmodel.GameViewModel
import java.time.Clock
import java.time.Instant
import java.util.*

@Composable
private fun ItemCardList(
    games: List<Game>,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Game) -> Unit,
    onChangeStatusClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    LazyColumn(modifier = modifier.animateContentSize()) {
        items(games) { game ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                exposedText = {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(game.status.toResource()).uppercase(),
                            color = game.status.toColor(),
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
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        val fields: Map<String?, Int> = mapOf(
                                game.platform  to R.string.insert_game_platform,
                                game.genre     to R.string.game_insert_genre,
                                game.developer to R.string.game_insert_developer,
                                game.publisher to R.string.game_insert_publisher
                        )
                        fields.forEach { entry ->
                           entry.key.takeIf { it != null && it != "" }
                                ?.let {
                                    CardSubtitleTextLabel(
                                        text = it,
                                        label = { Text(
                                            text = stringResource(entry.value),
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.secondary
                                        ) }
                                    )
                                }
                        }
                        game.completionDate?.let {
                            CardSubtitleTextLabel(
                                text = format.format(localDateFromEpochSecond(it)),
                                label = { Text(
                                    text = stringResource(R.string.game_form_completion_date_label),
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.secondary
                                ) }
                            )
                        }
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
private fun SubFab(
    text: String,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String
) {
    ExtendedFloatingActionButton(
        text = { Text(text = text, fontSize = 9.sp, color = MaterialTheme.colors.onPrimary) },
        onClick = onClick,
        icon = { Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp)
        ) },
        modifier = Modifier.height(36.dp),
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun BacklogFab(
    modifier: Modifier = Modifier,
    onOnlineSearchButtonClick: () -> Unit,
    onCreateButtonClick: () -> Unit,
    onSteamImportClick: () -> Unit
) {
    ActionsFab(
        icon = Icons.Default.Add,
        modifier = modifier
    ) {
        SubFab(
            text = stringResource(R.string.backlog_fab_create).uppercase(),
            onClick = onCreateButtonClick,
            imageVector = Icons.Default.Create,
            contentDescription = ""
        )
        SubFab(
            text = stringResource(R.string.online_search_title).uppercase(),
            onClick = onOnlineSearchButtonClick,
            imageVector = Icons.Default.Public,
            contentDescription = ""
        )
        SubFab(
            text = stringResource(R.string.steam_import_title).uppercase(),
            onClick = onSteamImportClick,
            imageVector = Icons.Default.LibraryBooks,
            contentDescription = ""
        )
    }
}

@Composable
private fun FilterDialog(state: GameFilterState) {
    Dialog(
        onDismissRequest = { state.shouldShowFilters = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(shape = LookAndFeel.DialogSurfaceShape) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.filters_heading), style = MaterialTheme.typography.h6)
                Text(text = stringResource(R.string.insert_status_label), style = MaterialTheme.typography.subtitle1)
                Column() {
                    state.statusFilters.forEach {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = it.value,
                                onCheckedChange = { newValue -> it.value = newValue }
                            )
                            Text(stringResource(it.nameResId))
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun BacklogTopBarExtra(
    crossinline onSearchClick: (String) -> Unit,
    noinline onFilterClick: () -> Unit
) {
    var searchValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    if (expanded) {
        SearchBar(
            value = searchValue,
            onValueChange = { searchValue = it } ,
            onSearchClick = {
                onSearchClick(searchValue)

                expanded = false
            }
        )
        IconButton(
            onClick = {
                searchValue = ""
                onSearchClick(searchValue)

                expanded = false
            }
        ) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close search")
        }
    } else {
        if (searchValue != "") {
            Text(text = searchValue, modifier = Modifier.alpha(0.5f))
            IconButton(
                onClick = {
                    searchValue = ""
                    onSearchClick(searchValue)
                }
            ) {
                Icon(imageVector = Icons.Default.Cancel, contentDescription = "Clear search")
            }
        }
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
        }
        IconButton(onClick = onFilterClick) {
            Icon(imageVector = Icons.Outlined.FilterList, contentDescription = "Filters")
        }
    }
}

// TODO Pass VM logic as lambdas
@Composable
fun BacklogScreen(
    onEditCardClick: (Int) -> Unit,
    gameViewModel: GameViewModel
) {
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
                    if (it == GameStatus.COMPLETED) {
                        gameViewModel.setCompletionDate(gameUidStatusUpdate.value!!, now())
                    }
                    resetChangeState()
                },
                onDismissRequest = resetChangeState,
                toColor = { it.toColor() },
                toResource = { it.toResource() }
            )
        }
    }
    if (gameViewModel.filterState.shouldShowFilters) {
        FilterDialog(gameViewModel.filterState)
    }

    gameViewModel.backlog
        .collectAsState(initial = listOf()).value
        .filter { gameViewModel.filterState.testAll(it) }
        .apply {
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
