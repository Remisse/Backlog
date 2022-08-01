package com.github.backlog

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.example.backlog.R
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.network.OnlineSearchClient
import com.github.backlog.model.network.OnlineSearchService
import com.github.backlog.util.ViewModelContainerAccessor
import com.github.backlog.util.ViewModelContainerAccessorImpl
import com.github.backlog.util.readRaw

class BacklogApplication : Application() {

    private val backlogDatabase: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            // TODO Delete this
            .fallbackToDestructiveMigration()
            .build()
    }
    private val searchCacheDatabase: SearchCacheDatabase by lazy {
        Room.databaseBuilder(applicationContext, SearchCacheDatabase::class.java, "search_cache_database")
            // TODO Delete this
            .fallbackToDestructiveMigration()
            .build()
    }

    private val onlineSearchClient = OnlineSearchClient(readRaw(R.raw.url))
    private val onlineSearchService: OnlineSearchService = onlineSearchClient.service

    val viewModelContainerAccessor: ViewModelContainerAccessor by lazy {
        ViewModelContainerAccessorImpl(
            backlogDatabase,
            searchCacheDatabase,
            WorkManager.getInstance(this),
            onlineSearchService
        )
    }
}
