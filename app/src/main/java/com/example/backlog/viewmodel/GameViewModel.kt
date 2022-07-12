package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.model.GameStatus
import com.example.backlog.model.TaskStatus
import com.example.backlog.model.database.dao.GameDao
import com.example.backlog.model.database.entity.Game
import com.example.backlog.ui.state.GameFormState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameViewModel(private val dao: GameDao) : ViewModel() {

    val backlog: Flow<List<Game>> = dao.backlog()

    fun entityById(uid: Int): Flow<Game> {
        return dao.gameById(uid)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entity: Game, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.insert(entity) != 0L) {
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

    fun update(entity: Game, onSuccess: () -> Unit, onFailure: () -> Unit): Job = viewModelScope.launch {
        if (dao.update(entity) != 0) {
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun setStatus(uid: Int, status: GameStatus) = viewModelScope.launch {
        dao.setStatus(uid, status)
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
