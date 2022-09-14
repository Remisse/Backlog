package com.github.backlog.utils

import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchService
import com.github.backlog.repository.OnlineSearchRepository
import com.github.backlog.viewmodel.*

class ViewModelFactoryStoreImpl(
    backlogDatabase: BacklogDatabase,
    searchCacheDatabase: SearchCacheDatabase,
    workManager: WorkManager,
    onlineSearchService: OnlineSearchService,
    profilePreferences: SharedPreferences,
    notificationBuilder: NotificationCompat.Builder
) : ViewModelFactoryStore {
    override val gameViewModelFactory by lazy {
        GameViewModelFactory(backlogDatabase.gamedao())
    }
    override val taskViewModelFactory by lazy {
        TaskViewModelFactory(backlogDatabase.taskdao(), workManager, notificationBuilder)
    }
    override val onlineSearchViewModelFactory by lazy {
        OnlineSearchViewModelFactory(OnlineSearchRepository(searchCacheDatabase, onlineSearchService))
    }
    override val profileViewModelFactory by lazy {
        ProfileViewModelFactory(profilePreferences)
    }
}
