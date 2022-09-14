package com.github.backlog

import android.app.Application
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.WorkManager
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchClient
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.utils.ViewModelFactoryStoreImpl

const val CHANNEL_ID = "deadline"

class BacklogApplication : Application() {
    private val backlogDatabase: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            .build()
    }
    private val searchCacheDatabase: SearchCacheDatabase by lazy {
        Room.databaseBuilder(applicationContext, SearchCacheDatabase::class.java, "search_cache_database")
            .build()
    }

    private val onlineSearchClient = OnlineSearchClient()

    val viewModelFactoryStore: ViewModelFactoryStore by lazy {
        ViewModelFactoryStoreImpl(
            backlogDatabase,
            searchCacheDatabase,
            WorkManager.getInstance(this),
            onlineSearchClient.service,
            getSharedPreferences("profile", MODE_PRIVATE),
            NotificationCompat.Builder(this, CHANNEL_ID)
        )
    }
}
