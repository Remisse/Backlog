package com.github.backlog.ui.screen.main

import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.components.BottomNavigationBar
import com.github.backlog.ui.components.TopMenuBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore
import kotlinx.coroutines.launch

abstract class MainScreen(
    private val drawerState: DrawerState,
    vmFactories: ViewModelFactoryStore
) : BaseScreen(vmFactories) {

    @Composable
    protected abstract fun TopBarExtraButtons()

    @Composable
    override fun TopBar() {
        val scope = rememberCoroutineScope()
        TopMenuBar(
            heading = section.resourceId,
            onMenuButtonClick = {
                scope.launch { drawerState.open() }
            },
            modifier = Modifier,
            extra = { TopBarExtraButtons() }
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) {
        BottomNavigationBar(navController = navController, sections = sections)
    }
}
