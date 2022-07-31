package com.github.backlog.util

import androidx.work.WorkManager
import com.github.backlog.model.database.BacklogDatabase
import com.github.backlog.ui.screen.ViewModelContainer
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.GameViewModelFactory
import com.github.backlog.viewmodel.TaskViewModel
import com.github.backlog.viewmodel.TaskViewModelFactory

/*
 * TODO
 *  Decide whether to leave this as-is or to refactor it using ViewModelStoreOwner and
 *  CompositionLocalProvider from Jetpack Compose.
 */
class ViewModelContainerAccessorImpl(database: BacklogDatabase,
                                     workManager: WorkManager
) : ViewModelContainerAccessor {

    private val gameViewModelFactory by lazy { GameViewModelFactory(database.gamedao()) }
    private val taskViewModelFactory by lazy { TaskViewModelFactory(database.taskdao(), workManager) }

    private fun createGameViewModel() = gameViewModelFactory.create(GameViewModel::class.java)
    private fun createTaskViewModel() = taskViewModelFactory.create(TaskViewModel::class.java)

    private var _viewModelContainer: ViewModelContainer

    override fun getViewModelContainer(): ViewModelContainer = _viewModelContainer

    init {
        _viewModelContainer = ViewModelContainer(
            gameViewModel = createGameViewModel(),
            taskViewModel = createTaskViewModel()
        )
    }

    override fun clear() {
        _viewModelContainer = ViewModelContainer(
            gameViewModel = createGameViewModel(),
            taskViewModel = createTaskViewModel()
        )
    }
}
