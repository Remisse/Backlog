package com.github.backlog.model.database.backlog.dao

import androidx.room.*
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.entity.Task
import com.github.backlog.model.database.backlog.entity.TaskWithGameTitle
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun allTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(task: Task): Long

    @Query("SELECT T.*, G.title AS gameTitle " +
            "FROM task T LEFT JOIN game G ON G.uid == T.gameId")
    fun tasksByGame(): Flow<List<TaskWithGameTitle>>

    @Query("UPDATE task " +
            "SET status = :status " +
            "WHERE uid == :taskId")
    suspend fun setTaskStatus(taskId: Int, status: TaskStatus)

    @Update
    suspend fun update(task: Task): Int

    @Query("SELECT * from task " +
            "WHERE task.uid == :taskId")
    fun taskById(taskId: Int): Flow<Task>

    @Query("DELETE FROM task " +
            "WHERE task.uid == :taskId")
    suspend fun delete(taskId: Int): Int
}
