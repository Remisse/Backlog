package com.github.backlog.model.database.backlog

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.backlog.model.Converters
import com.github.backlog.model.database.backlog.dao.GameDao
import com.github.backlog.model.database.backlog.dao.TaskDao
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.backlog.entity.Task

@Database(
    entities = [Game::class, Task::class],
    version = 21
)
@TypeConverters(Converters::class)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun taskdao(): TaskDao
}
