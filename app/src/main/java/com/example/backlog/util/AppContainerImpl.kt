package com.example.backlog.util

import androidx.work.WorkManager
import com.example.backlog.model.database.BacklogDatabase
import com.example.backlog.viewmodel.*

class AppContainerImpl(database: BacklogDatabase, workManager: WorkManager) : AppContainer {

    override val gameViewModelFactory by lazy { GameViewModelFactory(database.gamedao()) }
    override val taskViewModelFactory by lazy { TaskViewModelFactory(database.taskdao(), workManager) }
}
