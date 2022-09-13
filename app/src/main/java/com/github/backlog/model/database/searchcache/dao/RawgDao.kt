package com.github.backlog.model.database.searchcache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.entity.GameSlim
import kotlinx.coroutines.flow.Flow

@Dao
interface RawgDao {
    @Query("SELECT Game.* " +
            "FROM Game, GameSlim " +
            "WHERE Game.rawg_id = GameSlim.game_id " +
            "AND GameSlim.search = :searchQuery"
    )
    fun gamesBySearch(searchQuery: String): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGamesBySearch(gamesBySearch: List<GameSlim>)
}
