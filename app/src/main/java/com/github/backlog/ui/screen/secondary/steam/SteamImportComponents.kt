package com.github.backlog.ui.screen.secondary.steam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.theme.BacklogTheme
import java.util.stream.IntStream
import kotlin.streams.toList

@Composable
fun SteamImportContent(
    games: List<Game>,
    onConfirmClick: (Collection<Game>) -> Unit
) {
    val selectedGames: MutableState<Set<Game>> = remember { mutableStateOf(games.toSet()) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${games.count()} ${stringResource(R.string.steam_import_count)}",
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.padding(4.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(games) { game ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(.9f)
                    ) {
                        Text(game.title)
                    }
                    Checkbox(
                        checked = game in selectedGames.value,
                        onCheckedChange = {
                            if (game in selectedGames.value) {
                                selectedGames.value = selectedGames.value.minus(game)
                            } else {
                                selectedGames.value = selectedGames.value.plus(game)
                            }
                        }
                    )
                }
            }
        }
        Button(
            onClick = { onConfirmClick(selectedGames.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.steam_import_add_selected).uppercase())
        }
    }
}

@Preview
@Composable
fun SteamImportPreview() {
    val games: List<Game> = IntStream.range(0, 20)
        .mapToObj { Game(uid = it, title = "Preview", status = GameStatus.NOT_STARTED) }
        .toList()

    BacklogTheme() {
        Surface() {
            SteamImportContent(
                games = games,
                onConfirmClick = {}
            )
        }
    }
}
