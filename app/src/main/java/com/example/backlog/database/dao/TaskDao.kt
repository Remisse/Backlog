package com.example.backlog.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.backlog.database.entity.Task
import com.example.backlog.database.entity.TaskWithGameTitle
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(task: Task)

    @Query("SELECT T.*, G.title AS gameTitle " +
            "FROM game G, task T " +
            "WHERE G.uid == T.gameId " +
            "GROUP BY G.title")
    fun getTasksByGame(): Flow<List<TaskWithGameTitle>>
}
