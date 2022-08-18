package com.github.backlog.ui.screen.content.main.library

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.github.backlog.Section
import com.github.backlog.ui.screen.content.main.MainScreen
import com.github.backlog.ui.screen.content.main.library.components.BacklogFab
import com.github.backlog.ui.screen.content.main.library.components.BacklogScreen
import com.github.backlog.ui.screen.content.main.library.components.BacklogTopBarExtraButtons
import com.github.backlog.util.ViewModelContainer

open class LibraryScreen(private val onEditCardButtonClick: (Int) -> Unit,
                         private val onOnlineSearchButtonClick: () -> Unit,
                         private val onCreateButtonClick: () -> Unit,
                         viewModelContainer: ViewModelContainer
) : MainScreen(viewModelContainer) {

    override val section: Section = Section.Library

    @Composable
    override fun Content(arguments: Bundle?) {
        BacklogScreen(
            onEditCardClick = onEditCardButtonClick,
            gameViewModel = viewModelContainer.gameViewModel
        )
    }

    @Composable
    override fun Fab() {
        BacklogFab(
            onOnlineSearchButtonClick = onOnlineSearchButtonClick,
            onCreateButtonClick = onCreateButtonClick
        )
    }
    
    @Composable
    override fun TopBarExtraButtons() {
        BacklogTopBarExtraButtons(onClick = {
            viewModelContainer.gameViewModel.filterState.shouldShowFilters = true
        })
    }
}
