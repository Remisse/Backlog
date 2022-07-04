package com.example.backlog

import com.example.backlog.database.BacklogDatabase
import com.example.backlog.viewmodel.factory.GameViewModelFactory
import com.example.backlog.viewmodel.factory.TaskViewModelFactory

class AppContainer(private val database: BacklogDatabase) {

    val gameViewModelFactory = GameViewModelFactory(database.gamedao())

    val taskViewModelFactory = TaskViewModelFactory(database.taskdao())
}
