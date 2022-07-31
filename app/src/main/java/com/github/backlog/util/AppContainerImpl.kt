package com.github.backlog.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkManager
import com.github.backlog.model.database.BacklogDatabase
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.GameViewModelFactory
import com.github.backlog.viewmodel.TaskViewModel
import com.github.backlog.viewmodel.TaskViewModelFactory

class AppContainerImpl(database: BacklogDatabase, workManager: WorkManager) : AppContainer {

    private val gameViewModelFactory by lazy { GameViewModelFactory(database.gamedao()) }
    private val taskViewModelFactory by lazy { TaskViewModelFactory(database.taskdao(), workManager) }

    override fun createGameViewModel(): GameViewModel = gameViewModelFactory.create(GameViewModel::class.java)

    override fun createTaskViewModel(): TaskViewModel = taskViewModelFactory.create(TaskViewModel::class.java)
}
