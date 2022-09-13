package com.github.backlog.ui.screen

import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor

abstract class BaseScreen(private val accessor: ViewModelContainerAccessor) : BacklogScreen {
    protected fun viewModelContainer(): ViewModelContainer {
        return accessor.viewModelContainer
    }
}
