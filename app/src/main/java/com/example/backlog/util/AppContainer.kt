package com.example.backlog.util

import com.example.backlog.viewmodel.GameViewModelFactory
import com.example.backlog.viewmodel.TaskViewModelFactory

interface AppContainer {

    val gameViewModelFactory: GameViewModelFactory

    val taskViewModelFactory: TaskViewModelFactory
}