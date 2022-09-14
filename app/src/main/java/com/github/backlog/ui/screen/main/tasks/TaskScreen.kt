package com.github.backlog.ui.screen.main.tasks

import android.os.Bundle
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelFactoryStore

class TaskScreen(
    private val onTaskEditClick: (Int) -> Unit,
    private val onCreateClick: () -> Unit,
    drawerState: DrawerState,
    vmFactories: ViewModelFactoryStore
) : MainScreen(drawerState, vmFactories) {

    override val section: Section = Section.Tasks

    @Composable
    override fun Content(arguments: Bundle?) {
        TaskScreenContent(
            onTaskEditClick = onTaskEditClick,
            taskViewModel = taskViewModel()
        )
    }

    @Composable
    override fun Fab() {
        TaskFab(onCreateClick = onCreateClick)
    }

    @Composable
    override fun TopBarExtraButtons() {}
}
