package com.github.backlog

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.github.backlog.model.database.BacklogDatabase
import com.github.backlog.util.AppContainer
import com.github.backlog.util.AppContainerImpl

class BacklogApplication : Application() {

    private val database: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            // TODO Delete this
            .fallbackToDestructiveMigration()
            .build()
    }
    val appContainer: AppContainer by lazy {
        AppContainerImpl(
            database,
            WorkManager.getInstance(this)
        )
    }
}
