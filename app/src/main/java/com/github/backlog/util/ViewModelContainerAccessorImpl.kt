package com.github.backlog.util

import androidx.work.WorkManager
import com.github.backlog.model.database.backlog.BacklogDatabase
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.network.OnlineSearchService
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.GameViewModelFactory
import com.github.backlog.viewmodel.TaskViewModel
import com.github.backlog.viewmodel.TaskViewModelFactory

/*
 * TODO
 *  Decide whether to leave this as-is or to refactor it using ViewModelStoreOwner and
 *  CompositionLocalProvider from Jetpack Compose.
 */
class ViewModelContainerAccessorImpl(backlogDatabase: BacklogDatabase,
                                     searchCacheDatabase: SearchCacheDatabase,
                                     workManager: WorkManager,
                                     onlineSearchService: OnlineSearchService
) : ViewModelContainerAccessor {

    private val gameViewModelFactory by lazy { GameViewModelFactory(backlogDatabase.gamedao()) }
    private val taskViewModelFactory by lazy { TaskViewModelFactory(backlogDatabase.taskdao(), workManager) }

    private fun initContainer(): ViewModelContainer = ViewModelContainer(
        gameViewModelFactory.create(GameViewModel::class.java),
        taskViewModelFactory.create(TaskViewModel::class.java)
    )

    override var viewModelContainer: ViewModelContainer = initContainer()
        private set

    override fun clear() {
        viewModelContainer = initContainer()
    }
}
