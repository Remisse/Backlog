package com.github.backlog.utils

import androidx.lifecycle.ViewModelProvider

interface ViewModelFactoryStore {
    val gameViewModelFactory: ViewModelProvider.Factory

    val taskViewModelFactory: ViewModelProvider.Factory

    val onlineSearchViewModelFactory: ViewModelProvider.Factory

    val profileViewModelFactory: ViewModelProvider.Factory
}
