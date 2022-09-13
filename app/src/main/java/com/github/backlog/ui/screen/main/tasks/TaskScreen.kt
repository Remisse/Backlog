package com.github.backlog.ui.screen.main.tasks

import android.os.Bundle
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor

class TaskScreen(
    private val onTaskEditClick: (Int) -> Unit,
    private val onCreateClick: () -> Unit,
    drawerState: DrawerState,
    accessor: ViewModelContainerAccessor
) : MainScreen(drawerState, accessor) {

    override val section: Section = Section.Tasks

    @Composable
    override fun Content(arguments: Bundle?) {
        TaskScreenContent(
            onTaskEditClick = onTaskEditClick,
            taskViewModel = viewModelContainer().taskViewModel
        )
    }

    @Composable
    override fun Fab() {
        TaskFab(onCreateClick = onCreateClick)
    }

    @Composable
    override fun TopBarExtraButtons() {}
}
