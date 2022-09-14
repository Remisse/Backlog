package com.github.backlog.ui.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.OnlineSearchViewModel
import com.github.backlog.viewmodel.ProfileViewModel
import com.github.backlog.viewmodel.TaskViewModel

abstract class BaseScreen(private val vmFactories: ViewModelFactoryStore)
    : BacklogScreen {
    override val viewModelStoreOwner = object : ViewModelStoreOwner {
        private val viewModelStore = ViewModelStore()

        override fun getViewModelStore(): ViewModelStore {
            return viewModelStore
        }
    }

    @Composable
    private inline fun <reified VM : ViewModel> getViewModel(factory: ViewModelProvider.Factory): VM {
        return viewModel(viewModelStoreOwner = viewModelStoreOwner, factory = factory)
    }

    @Composable
    protected fun gameViewModel(): GameViewModel =
        getViewModel(factory = vmFactories.gameViewModelFactory)

    @Composable
    protected fun taskViewModel(): TaskViewModel =
        getViewModel(factory = vmFactories.taskViewModelFactory)

    @Composable
    protected fun onlineSearchViewModel(): OnlineSearchViewModel =
        getViewModel(factory = vmFactories.onlineSearchViewModelFactory)

    @Composable
    protected fun profileViewModel(): ProfileViewModel =
        getViewModel(factory = vmFactories.profileViewModelFactory)
}
