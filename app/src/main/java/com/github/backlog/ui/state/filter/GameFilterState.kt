package com.github.backlog.ui.state.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.toResource

// TODO Refactor using Composite
class GameFilterState {
    var shouldShowFilters by mutableStateOf(false)

    val statusFilters: List<BooleanFilter<Game>> = GameStatus.values().map {
        BooleanFilter(nameResId = it.toResource(), initialValue = true) { game, value ->
            value && game.status == it
        }
    }

    val titleFilter: StringFilter<Game> =
        StringFilter(nameResId = R.string.insert_game_title, initialValue = "") { game, value ->
            value.split(" ")
                .all {
                    game.title.lowercase()
                        .contains(it)
                }

        }

    fun testAll(game: Game): Boolean {
        return statusFilters.any { it.test(game) }
                && titleFilter.test(game)
    }
}
