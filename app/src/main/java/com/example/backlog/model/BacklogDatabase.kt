package com.example.backlog.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.backlog.model.dao.GameDao
import com.example.backlog.model.entities.Game

@Database(entities = [Game::class], version = 1)
abstract class BacklogDatabase() : RoomDatabase() {

    abstract fun gamedao(): GameDao
}
