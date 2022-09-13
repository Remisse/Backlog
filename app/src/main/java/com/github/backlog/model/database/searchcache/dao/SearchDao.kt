package com.github.backlog.model.database.searchcache.dao

import androidx.room.*
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.entity.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM Search")
    fun searches(): Flow<List<Search>>

    @Query("SELECT * FROM Game")
    fun allGames(): Flow<List<Game>>

    @Query("SELECT Search.timestamp " +
            "FROM Search " +
            "WHERE Search.query = :query")
    fun getSearchTimestamp(query: String): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: Search): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailedGameWithReplace(game: Game)
}
