package com.github.backlog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.repository.OnlineSearchRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import java.net.SocketTimeoutException

class OnlineSearchViewModel(private val repository: OnlineSearchRepository) : ViewModel() {

    var isNetworkError by mutableStateOf(false)
        private set
    var isErrorShown by mutableStateOf(false)

    private inline fun tryWrapper(runnable: () -> Unit) {
        try {
            runnable()
            isNetworkError = false
        } catch (e: Exception) { // TODO Handle exceptions in a more precise way
            isNetworkError = true
            isErrorShown = false
        }
    }

    fun performSearch(query: String): Flow<List<Game>> {
        viewModelScope.launch {
            tryWrapper {
                repository.cacheRawgSearch(query)
            }
        }

        return repository.getGamesByRawgSearch(query)
    }

    suspend fun retrieveGameDetails(rawgId: String): Flow<Game> {
        val game: Flow<Game> = flow {
            tryWrapper {
                val result = repository.getGameDetailsFromRawg(rawgId)
                emit(result)
            }
        }

        return game
    }

    fun retrieveSteamLibrary(steamId: String): Flow<List<Game>> {
        viewModelScope.launch {
            tryWrapper {
                repository.cacheSteamImport(steamId)
            }
        }

        return repository.getGamesBySteamImport(steamId)
    }
}

class OnlineSearchViewModelFactory(private val repository: OnlineSearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnlineSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnlineSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
