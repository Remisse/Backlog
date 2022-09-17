package com.github.backlog.ui.screen.main.library

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.*
import com.github.backlog.ui.state.filter.GameFilterState
import com.github.backlog.utils.localDateFromEpochSecond
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Steam
import compose.icons.tablericons.LetterR
import kotlinx.coroutines.flow.Flow
import java.util.*

@Composable
private fun ItemCardList(
    games: List<Game>,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onChangeStatusClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    LazyColumn(modifier = modifier.animateContentSize()) {
        items(games) { game ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                exposedText = {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(game.status.toResource()),
                            color = game.status.toColor(),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = game.title,
                            style = MaterialTheme.typography.titleSmall
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
                                    TextLabel(
                                        text = {
                                           Text(
                                               text = it,
                                               style = MaterialTheme.typography.bodyMedium,
                                               modifier = Modifier.alpha(.75f)
                                           )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(entry.value),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    )
                                }
                        }
                        game.completionDate?.let {
                            TextLabel(
                                text = {
                                    Text(
                                        text = format.format(localDateFromEpochSecond(it)),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.alpha(.75f)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.game_form_completion_date_label),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            )
                        }
                    }
                },
                onChangeStatusClick = { onChangeStatusClick(game.uid) },
                onEditClick = { onEditClick(game.uid) },
                onDeleteClick = { onDeleteClick(game.uid) }
            )
        }
    }
}

@Composable
private fun SubButton(
    text: String,
    onClick: () -> Unit,
    imageVector: ImageVector,
    iconSize: Dp = Dp.Unspecified,
    contentDescription: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        FilledIconButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize)
            )
        }
    }
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
        SubButton(
            text = stringResource(R.string.backlog_fab_create),
            onClick = onCreateButtonClick,
            imageVector = Icons.Default.Create,
            contentDescription = ""
        )
        SubButton(
            text = stringResource(R.string.online_search_title),
            onClick = onOnlineSearchButtonClick,
            imageVector = TablerIcons.LetterR,
            contentDescription = ""
        )
        SubButton(
            text = stringResource(R.string.steam_import_title),
            onClick = onSteamImportClick,
            imageVector = FontAwesomeIcons.Brands.Steam,
            contentDescription = "",
            iconSize = 22.dp
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
                Text(
                    text = stringResource(R.string.filters_heading),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Text(
                    text = stringResource(R.string.insert_status_label),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
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
fun BacklogTopBarExtra(filterState: GameFilterState) {
    var searchValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    if (expanded) {
        SearchBar(
            value = searchValue,
            onValueChange = { searchValue = it } ,
            onSearchClick = {
                filterState.titleFilter.value = searchValue
                expanded = false
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(.8f)
        )
        IconButton(
            onClick = {
                searchValue = ""
                filterState.titleFilter.value = searchValue
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
                    filterState.titleFilter.value = searchValue
                }
            ) {
                Icon(imageVector = Icons.Default.Cancel, contentDescription = "Clear search")
            }
        }
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
        }
        IconButton(onClick = { filterState.shouldShowFilters = true }) {
            Icon(imageVector = Icons.Outlined.FilterList, contentDescription = "Filters")
        }
    }
}

@Composable
fun BacklogScreen(
    onEditCardClick: (Int) -> Unit,
    onStatusChangeClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    backlog: Flow<List<Game>>,
    filterState: GameFilterState
) {
    if (filterState.shouldShowFilters) {
        FilterDialog(filterState)
    }

    backlog.collectAsState(initial = emptyList())
        .value
        .filter { filterState.testAll(it) }
        .apply {
            if (this.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.backlog_empty),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                ItemCardList(
                    games = this,
                    onChangeStatusClick = onStatusChangeClick,
                    onEditClick = onEditCardClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
}
