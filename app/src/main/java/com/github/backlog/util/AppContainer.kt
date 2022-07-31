package com.github.backlog.util

import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.TaskViewModel

interface AppContainer {

    fun createGameViewModel(): GameViewModel

    fun createTaskViewModel(): TaskViewModel
}