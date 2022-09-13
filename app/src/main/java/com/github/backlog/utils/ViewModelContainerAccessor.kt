package com.github.backlog.utils

// TODO Refactor
interface ViewModelContainerAccessor {

    val viewModelContainer: ViewModelContainer

    /**
     * Reinstantiates all ViewModels. To be called on each destination change so as to emulate
     * the default behaviour of VMs in Compose.
     */
    fun clear()
}
