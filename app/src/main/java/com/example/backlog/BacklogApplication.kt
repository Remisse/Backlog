package com.example.backlog

import android.app.Application
import androidx.room.Room
import com.example.backlog.model.BacklogDatabase

class BacklogApplication : Application() {

    private lateinit var database: BacklogDatabase
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        database = lazy {
            Room.databaseBuilder(this.applicationContext, BacklogDatabase::class.java,
                "backlog_database"
            ).build()
        }.value

        appContainer = AppContainer(database)
    }


}
