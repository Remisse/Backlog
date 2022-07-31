package com.github.backlog.ui.screen.content.secondary.gameform

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.components.SubScreenTopBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.ui.screen.ViewModelContainer

abstract class BaseGameForm(viewModelContainer: ViewModelContainer) : BaseScreen(viewModelContainer) {

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) { /* Empty */ }

    @Composable
    override fun TopBar() {
        SubScreenTopBar(
            heading = section.resourceId,
            onBackClick = { viewModelContainer.gameViewModel.formState.showCancelDialog = true }
        )
    }

    @Composable
    override fun Fab() { /* Empty */ }
}
