package com.github.backlog.ui.screen.main.library

import android.os.Bundle
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelFactoryStore

open class LibraryScreen(private val onEditCardButtonClick: (Int) -> Unit,
                         private val onOnlineSearchButtonClick: () -> Unit,
                         private val onCreateButtonClick: () -> Unit,
                         private val onSteamImportClick: () -> Unit,
                         drawerState: DrawerState,
                         vmFactories: ViewModelFactoryStore
) : MainScreen(drawerState, vmFactories) {
    override val section: Section = Section.Library

    @Composable
    override fun Content(arguments: Bundle?) {
        BacklogScreen(
            onEditCardClick = onEditCardButtonClick,
            gameViewModel = gameViewModel()
        )
    }

    @Composable
    override fun Fab() {
        BacklogFab(
            onOnlineSearchButtonClick = onOnlineSearchButtonClick,
            onCreateButtonClick = onCreateButtonClick,
            onSteamImportClick = onSteamImportClick
        )
    }
    
    @Composable
    override fun TopBarExtraButtons() {
        val vm = gameViewModel()
        BacklogTopBarExtra(
            onSearchClick = {
                vm.filterState.titleFilter.value = it
            },
            onFilterClick = {
                vm.filterState.shouldShowFilters = true
            }
        )
    }
}
