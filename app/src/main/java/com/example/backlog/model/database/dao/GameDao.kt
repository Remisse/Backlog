package com.example.backlog.model.database.dao

import androidx.room.*
import com.example.backlog.model.database.entity.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun backlog(): Flow<List<Game>>

    @Query("SELECT * FROM game " +
            "WHERE game.uid == :uid")
    fun gameById(uid: Int): Flow<Game>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game): Long

    @Query("DELETE FROM game " +
            "WHERE game.uid == :uid")
    suspend fun delete(uid: Int): Int
}
