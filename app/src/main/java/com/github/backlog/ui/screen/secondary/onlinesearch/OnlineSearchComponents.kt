package com.github.backlog.ui.screen.secondary.onlinesearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.LookAndFeel
import com.github.backlog.ui.components.SearchBar
import kotlinx.coroutines.flow.Flow
import java.util.stream.IntStream
import kotlin.streams.toList

@Composable
fun TopSearchBar(
    searchValue: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearchSubmit: () -> Unit
) {
    TopAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            SearchBar(
                value = searchValue,
                onValueChange = onValueChange,
                onSearchClick = onSearchSubmit,
                modifier = LookAndFeel.FieldModifier.height(40.dp)
            )
        }
    }
}

@Composable
private fun ResultList(
    results: List<Game>,
    onGameClick: (Game) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        items(results) { item ->
            Button(
                onClick = { onGameClick(item) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary
                )
            ) {
                Text(
                    text = item.title,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun OnlineSearchContent(
    onGameClick: (String) -> Unit,
    results: Flow<List<Game>>,
    searchPerformed: Boolean
) {
    if (!searchPerformed) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.online_search_empty),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    } else {
        val games = results.collectAsState(initial = emptyList())

        if (games.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.online_search_performed_wait),
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                CircularProgressIndicator()
            }
        } else {
            ResultList(results = games.value, onGameClick = { onGameClick(it.rawgId!!) })
        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    val searchValue = remember { mutableStateOf("") }

    Surface() {
        TopSearchBar(
            searchValue = searchValue.value,
            onValueChange = { },
            onBackClick = { },
            onSearchSubmit = { },
        )
    }
}

@Preview
@Composable
private fun OnlineSearchContentPreview() {
    val results = IntStream.range(0, 10)
        .mapToObj { Game(
            title = "This is a preview",
            status = GameStatus.NOT_STARTED,
            coverPath = null
        ) }
        .toList()

    ResultList(results = results, onGameClick = { })
}
