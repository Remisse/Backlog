package com.github.backlog.ui.screen.secondary.gameform

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.components.SubScreenTopBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore

abstract class BaseGameForm(vmFactories: ViewModelFactoryStore) : BaseScreen(vmFactories) {
    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) { /* Empty */ }

    @Composable
    override fun TopBar() {
        val gameViewModel = gameViewModel()
        SubScreenTopBar(
            heading = section.resourceId,
            onBackClick = { gameViewModel.formState.showCancelDialog = true }
        )
    }

    @Composable
    override fun Fab() { /* Empty */ }
}
