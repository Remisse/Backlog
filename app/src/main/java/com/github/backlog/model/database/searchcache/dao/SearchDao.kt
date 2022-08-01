package com.github.backlog.model.database.searchcache.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.entity.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM Search")
    fun searches(): Flow<List<Search>>

    @Query("SELECT Game.* " +
            "FROM Game, Search " +
            "WHERE :searchQuery == Search.query")
    fun cachedGamesFromSearch(searchQuery: String): Flow<List<Game>>
}
