package com.github.backlog.ui.screen.main.library

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.*
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelFactoryStore

open class LibraryScreen(
    private val onEditCardButtonClick: (Int) -> Unit,
    private val onOnlineSearchButtonClick: () -> Unit,
    private val onCreateButtonClick: () -> Unit,
    private val onSteamImportClick: () -> Unit,
    private val onStatusChangeClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    vmFactories: ViewModelFactoryStore
) : MainScreen(vmFactories) {
    override val section: Section = Section.Library

    @Composable
    override fun Content(arguments: Bundle?) {
        BacklogScreen(
            onEditCardClick = onEditCardButtonClick,
            onStatusChangeClick = onStatusChangeClick,
            onDeleteClick = onDeleteClick,
            backlog = gameViewModel().backlog,
            filterState = gameViewModel().filterState
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
        BacklogTopBarExtra(filterState = gameViewModel().filterState)
    }
}
