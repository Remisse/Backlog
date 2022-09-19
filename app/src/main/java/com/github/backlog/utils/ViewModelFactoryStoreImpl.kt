package com.github.backlog.utils

import android.content.SharedPreferences
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.onlineservice.OnlineSearchService
import com.github.backlog.repository.OnlineSearchRepository
import com.github.backlog.viewmodel.GameViewModelFactory
import com.github.backlog.viewmodel.OnlineSearchViewModelFactory
import com.github.backlog.viewmodel.ProfileViewModelFactory
import com.github.backlog.viewmodel.TaskViewModelFactory

class ViewModelFactoryStoreImpl(
    backlogDatabase: BacklogDatabase,
    searchCacheDatabase: SearchCacheDatabase,
    onlineSearchService: OnlineSearchService,
    profilePreferences: SharedPreferences,
) : ViewModelFactoryStore {
    override val gameViewModelFactory by lazy {
        GameViewModelFactory(backlogDatabase.gamedao())
    }
    override val taskViewModelFactory by lazy {
        TaskViewModelFactory(backlogDatabase.taskdao())
    }
    override val onlineSearchViewModelFactory by lazy {
        OnlineSearchViewModelFactory(OnlineSearchRepository(searchCacheDatabase, onlineSearchService))
    }
    override val profileViewModelFactory by lazy {
        ProfileViewModelFactory(profilePreferences)
    }
}
