package com.example.backlog

import android.app.Application
import androidx.room.Room
import com.example.backlog.database.BacklogDatabase

class BacklogApplication : Application() {

    private val database: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            // TODO Delete this
            .fallbackToDestructiveMigration()
            .build()
    }
    val appContainer: AppContainer by lazy { AppContainer(database) }
}
