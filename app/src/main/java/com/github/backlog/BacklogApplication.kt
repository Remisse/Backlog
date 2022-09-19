package com.github.backlog

import android.app.Application
import androidx.room.Room
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchClient
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.utils.ViewModelFactoryStoreImpl

class BacklogApplication : Application() {
    val backlogDatabase: BacklogDatabase by lazy {
        Room.databaseBuilder(applicationContext, BacklogDatabase::class.java, "backlog_database")
            .build()
    }
    private val searchCacheDatabase: SearchCacheDatabase by lazy {
        Room.databaseBuilder(applicationContext, SearchCacheDatabase::class.java, "search_cache_database")
            .build()
    }

    // Retrofit client
    private val onlineSearchClient = OnlineSearchClient()

    // ViewModel factory
    val viewModelFactoryStore: ViewModelFactoryStore by lazy {
        ViewModelFactoryStoreImpl(
            backlogDatabase,
            searchCacheDatabase,
            onlineSearchClient.service,
            getSharedPreferences("profile", MODE_PRIVATE)
        )
    }
}
