package com.example.backlog.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.backlog.database.dao.GameDao
import com.example.backlog.database.dao.TaskDao
import com.example.backlog.database.entities.Game
import com.example.backlog.database.entities.Task

@Database(
    entities = [Game::class, Task::class],
    version = 5
)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun taskdao(): TaskDao
}
