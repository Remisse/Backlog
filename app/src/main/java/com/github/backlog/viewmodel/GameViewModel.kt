package com.github.backlog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.dao.GameDao
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.backlog.queryentity.IntByString
import com.github.backlog.ui.state.filter.GameFilterState
import com.github.backlog.ui.state.form.GameFormState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameViewModel(private val dao: GameDao) : ViewModel() {
    val formState: GameFormState = GameFormState()
    val filterState: GameFilterState = GameFilterState()

    val backlog: Flow<List<Game>> = dao.backlog()

    val completedGamesByGenre: Flow<List<IntByString>> = dao.completedGamesByGenreTopFive()
    val droppedGamesByGenre: Flow<List<IntByString>> = dao.droppedGamesByGenreTopFive()
    val completedGamesByDeveloper: Flow<List<IntByString>> = dao.completedGamesByDeveloperTopFive()
    val droppedGamesByDeveloper: Flow<List<IntByString>> = dao.droppedGamesByDeveloperTopFive()
    val completedGamesInCurrentYear: Flow<Int> = dao.completedGamesInCurrentYear()

    fun entityById(uid: Int): Flow<Game> {
        return dao.gameById(uid)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entity: Game, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.insert(entity) != 0L) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun insertAll(entities: List<Game>, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.insertAll(entities).isNotEmpty()) { // TODO Handle case in which not all games are inserted?
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.delete(uid) != 0) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun update(entity: Game, onSuccess: () -> Unit, onFailure: () -> Unit): Job {
        return viewModelScope.launch {
            if (dao.update(entity) != 0) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun setStatus(uid: Int, status: GameStatus) {
        viewModelScope.launch {
            dao.setStatus(uid, status)
        }
    }

    fun setCompletionDate(uid: Int, date: Long) {
        viewModelScope.launch {
            dao.setCompletionDate(uid, date)
        }
    }
}

class GameViewModelFactory(private val dao: GameDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
