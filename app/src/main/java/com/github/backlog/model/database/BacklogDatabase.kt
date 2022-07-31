package com.github.backlog.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.backlog.model.database.dao.GameDao
import com.github.backlog.model.database.dao.TaskDao
import com.github.backlog.model.database.entity.Game
import com.github.backlog.model.database.entity.Task

@Database(
    entities = [Game::class, Task::class],
    version = 11
)
@TypeConverters(Converters::class)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun taskdao(): TaskDao
}
