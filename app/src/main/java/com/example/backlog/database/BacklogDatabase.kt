package com.example.backlog.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.backlog.database.dao.GameDao
import com.example.backlog.database.dao.TaskDao
import com.example.backlog.database.entity.Game
import com.example.backlog.database.entity.Task

@Database(
    entities = [Game::class, Task::class],
    version = 6
)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun taskdao(): TaskDao
}
