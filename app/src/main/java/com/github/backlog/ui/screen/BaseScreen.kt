package com.github.backlog.ui.screen

import com.github.backlog.util.AppContainer

abstract class BaseScreen(appContainer: AppContainer) : BacklogScreen {

    internal val viewModelContainer = ViewModelContainer(
        gameViewModel = appContainer.createGameViewModel(),
        taskViewModel = appContainer.createTaskViewModel()
    )
}
