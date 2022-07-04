package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.database.dao.GameDao
import com.example.backlog.database.entity.Game
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

