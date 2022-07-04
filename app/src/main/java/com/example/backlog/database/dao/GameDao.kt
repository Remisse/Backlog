package com.example.backlog.database.dao

import androidx.room.*
import com.example.backlog.database.entity.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun getBacklog(): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)
}