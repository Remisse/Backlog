package com.example.backlog.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.backlog.model.database.dao.GameDao
import com.example.backlog.model.database.dao.TaskDao
import com.example.backlog.model.database.entity.Game
import com.example.backlog.model.database.entity.Task

@Database(
    entities = [Game::class, Task::class],
    version = 9
)
@TypeConverters(Converters::class)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun taskdao(): TaskDao
}
