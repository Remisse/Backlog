package com.example.backlog.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.backlog.model.entities.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun getBacklog(): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)
}