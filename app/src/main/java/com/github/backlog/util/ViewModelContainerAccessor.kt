package com.github.backlog.util

import com.github.backlog.ui.screen.ViewModelContainer

interface ViewModelContainerAccessor {

    fun getViewModelContainer(): ViewModelContainer

    /**
     * Reinstantiates all ViewModels. To be called on each destination change so as to emulate
     * the default behaviour of VMs in Compose (see NavController::addOnDestinationChangedListener).
     */
    fun clear()
}
