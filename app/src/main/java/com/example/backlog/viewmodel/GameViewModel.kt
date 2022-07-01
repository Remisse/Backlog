package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.model.dao.GameDao
import com.example.backlog.model.entities.Game
import kotlinx.coroutines.launch

class GameViewModel(private val dao: GameDao) : ViewModel() {

    val backlog: LiveData<List<Game>> = dao.getBacklog().asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(game: Game) = viewModelScope.launch {
        dao.insert(game)
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
