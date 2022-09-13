package com.github.backlog.utils

import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchService
import com.github.backlog.repository.OnlineSearchRepository
import com.github.backlog.viewmodel.*

// TODO Refactor
class ViewModelContainerAccessorImpl(
    backlogDatabase: BacklogDatabase,
    searchCacheDatabase: SearchCacheDatabase,
    workManager: WorkManager,
    onlineSearchService: OnlineSearchService,
    profilePreferences: SharedPreferences,
    notificationBuilder: NotificationCompat.Builder
) : ViewModelContainerAccessor {
    private val gameViewModelFactory by lazy {
        GameViewModelFactory(backlogDatabase.gamedao())
    }
    private val taskViewModelFactory by lazy {
        TaskViewModelFactory(backlogDatabase.taskdao(), workManager, notificationBuilder)
    }
    private val onlineSearchViewModelFactory by lazy {
        OnlineSearchViewModelFactory(OnlineSearchRepository(searchCacheDatabase, onlineSearchService))
    }
    private val profileViewModelFactory by lazy {
        ProfileViewModelFactory(profilePreferences)
    }

    private fun initContainer(): ViewModelContainer = ViewModelContainer(
        gameViewModelFactory.create(GameViewModel::class.java),
        taskViewModelFactory.create(TaskViewModel::class.java),
        onlineSearchViewModelFactory.create(OnlineSearchViewModel::class.java),
        profileViewModelFactory.create(ProfileViewModel::class.java)
    )

    override var viewModelContainer: ViewModelContainer = initContainer()
        private set

    override fun clear() {
        viewModelContainer = initContainer()
    }
}
