package com.example.backlog

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.example.backlog.model.database.BacklogDatabase
<<<<<<< HEAD
import com.example.backlog.util.AppContainer
=======
>>>>>>> 66403a254aaacfbf6dcc505b3579f64651e861b8
import com.example.backlog.util.AppContainerImpl

class BacklogApplication : Application() {

    private val database: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            // TODO Delete this
            .fallbackToDestructiveMigration()
            .build()
    }
<<<<<<< HEAD
    val appContainer: AppContainer by lazy { AppContainerImpl(database, WorkManager.getInstance(this)) }
=======
    val appContainer: AppContainerImpl by lazy { AppContainerImpl(database, WorkManager.getInstance(this)) }
>>>>>>> 66403a254aaacfbf6dcc505b3579f64651e861b8
}
