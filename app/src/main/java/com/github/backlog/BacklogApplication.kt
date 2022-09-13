package com.github.backlog

import android.app.Application
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.WorkManager
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchClient
import com.github.backlog.utils.ViewModelContainerAccessor
import com.github.backlog.utils.ViewModelContainerAccessorImpl

const val CHANNEL_ID = "deadline"

class BacklogApplication : Application() {
    private val backlogDatabase: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
                // TODO Delete
            .fallbackToDestructiveMigration()
            .build()
    }
    private val searchCacheDatabase: SearchCacheDatabase by lazy {
        Room.databaseBuilder(applicationContext, SearchCacheDatabase::class.java, "search_cache_database")
            .build()
    }

    private val onlineSearchClient = OnlineSearchClient()

    val viewModelContainerAccessor: ViewModelContainerAccessor by lazy {
        ViewModelContainerAccessorImpl(
            backlogDatabase,
            searchCacheDatabase,
            WorkManager.getInstance(this),
            onlineSearchClient.service,
            getSharedPreferences("profile", MODE_PRIVATE),
            NotificationCompat.Builder(this, CHANNEL_ID)
        )
    }
}
