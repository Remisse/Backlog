package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.model.database.dao.GameDao
import com.example.backlog.model.database.entity.Game
import com.example.backlog.ui.state.GameFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception

class GameViewModel(private val dao: GameDao) : ViewModel() {

    val formState = GameFormState()

    val backlog: Flow<List<Game>> = dao.backlog()

    fun gameById(uid: Int): Flow<Game> {
        return dao.gameById(uid)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(game: Game, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.insert(game) != 0L) {
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.delete(uid) != 0) {
            onSuccess()
        } else {
            onFailure()
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
