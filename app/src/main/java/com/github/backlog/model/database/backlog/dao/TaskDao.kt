package com.github.backlog.model.database.backlog.dao

import androidx.room.*
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.entity.Task
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun allTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(task: Task): Long

    @Query("SELECT Task.*, Game.title AS gameTitle " +
            "FROM task LEFT JOIN game ON Game.uid = game_id")
    fun tasksByGame(): Flow<List<TaskWithGameTitle>>

    @Query("SELECT Task.*, Game.title AS gameTitle " +
            "FROM task LEFT JOIN game ON Game.uid = game_id " +
            "WHERE Task.uid = :taskId")
    fun taskWithGameTitleById(taskId: Int): Flow<TaskWithGameTitle>

    @Query("UPDATE task " +
            "SET status = :status " +
            "WHERE uid = :taskId")
    suspend fun setTaskStatus(taskId: Int, status: TaskStatus)

    @Update
    suspend fun update(task: Task): Int

    @Query("SELECT * from task " +
            "WHERE task.uid = :taskId")
    fun taskById(taskId: Int): Flow<Task>

    @Query("DELETE FROM task " +
            "WHERE task.uid = :taskId")
    suspend fun delete(taskId: Int): Int

    @Query("UPDATE Task " +
            "SET notified = 1 " +
            "WHERE uid = :taskId")
    suspend fun markAsNotified(taskId: Int): Int

    @Query("SELECT Task.*, Game.title AS gameTitle " +
            "FROM task LEFT JOIN game ON Game.uid = game_id " +
            "WHERE notified = 0 " +
            "AND deadline IS NOT NULL")
    fun nonNotifiedTasksWithDeadline(): Flow<List<TaskWithGameTitle>>

    // TODO
    // fun dueTasks()
}
