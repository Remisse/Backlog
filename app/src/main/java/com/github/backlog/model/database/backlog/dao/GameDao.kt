package com.github.backlog.model.database.backlog.dao

import androidx.room.*
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.backlog.queryentity.IntByString
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM Game")
    fun backlog(): Flow<List<Game>>

    @Query("SELECT * FROM game " +
            "WHERE game.uid = :uid")
    fun gameById(uid: Int): Flow<Game>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(game: Game): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(games: List<Game>): List<Long>

    @Query("DELETE FROM Game " +
            "WHERE Game.uid = :uid")
    suspend fun delete(uid: Int): Int

    @Update
    suspend fun update(game: Game): Int

    @Query("UPDATE Game " +
            "SET status = :status " +
            "WHERE uid = :uid ")
    suspend fun setStatus(uid: Int, status: GameStatus): Int

    @Query("UPDATE Game " +
            "SET completion_date = :date " +
            "WHERE uid = :uid " +
            "AND status = 'COMPLETED'")
    suspend fun setCompletionDate(uid: Int, date: Long): Int

    @Query("SELECT genre AS field, COUNT(*) AS `count` " +
            "FROM Game " +
            "WHERE status = 'COMPLETED' " +
            "AND genre IS NOT NULL " +
            "GROUP BY genre " +
            "ORDER BY `count` DESC " +
            "LIMIT 5")
    fun completedGamesByGenreTopFive(): Flow<List<IntByString>>

    @Query("SELECT genre AS field, COUNT(*) AS `count` " +
            "FROM Game " +
            "WHERE status = 'DROPPED' " +
            "AND genre IS NOT NULL " +
            "GROUP BY genre " +
            "ORDER BY `count` DESC " +
            "LIMIT 5")
    fun droppedGamesByGenreTopFive(): Flow<List<IntByString>>

    @Query("SELECT developer AS field, COUNT(*) AS `count` " +
            "FROM Game " +
            "WHERE status = 'COMPLETED' " +
            "AND developer IS NOT NULL " +
            "GROUP BY developer " +
            "ORDER BY `count` DESC " +
            "LIMIT 5")
    fun completedGamesByDeveloperTopFive(): Flow<List<IntByString>>

    @Query("SELECT developer AS field, COUNT(*) AS `count` " +
            "FROM Game " +
            "WHERE status = 'DROPPED' " +
            "AND developer IS NOT NULL " +
            "GROUP BY developer " +
            "ORDER BY `count` DESC " +
            "LIMIT 5")
    fun droppedGamesByDeveloperTopFive(): Flow<List<IntByString>>

    @Query("SELECT COUNT(*) " +
            "FROM Game " +
            "WHERE Game.completion_date IS NOT NULL " +
            "AND Game.completion_date >= CAST(strftime('%s','now','start of year','localtime') AS INTEGER) " +
            "AND Game.completion_date < CAST(strftime('%s','now','+1 year','start of year','localtime') AS INTEGER) ")
    fun completedGamesInCurrentYear(): Flow<Int>
}
