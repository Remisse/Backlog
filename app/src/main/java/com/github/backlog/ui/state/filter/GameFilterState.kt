package com.github.backlog.ui.state.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.entity.Game
import com.github.backlog.ui.components.gameStatusToResource

// TODO Refactor using Composite
class GameFilterState {
    var shouldShowFilters by mutableStateOf(false)

    val statusFilters: List<BooleanFilter<Game>> = GameStatus.values().map {
        BooleanFilter(nameResId = gameStatusToResource(it), initialValue = true) { game, value ->
            value && game.status == it
        }
    }
    val infoFilters: List<StringFilter<Game>> = listOf(
        StringFilter(nameResId = R.string.insert_game_title, initialValue = "") { game, value -> game.title.contains(value) },
        StringFilter(nameResId = R.string.insert_game_platform, initialValue = "") { game, value -> game.platform.contains(value) }
    )

    fun testAll(game: Game): Boolean {
        return statusFilters.any { it.test(game) }
                && infoFilters.all { it.test(game) }
    }
}
