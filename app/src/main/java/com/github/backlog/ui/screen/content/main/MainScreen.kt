package com.github.backlog.ui.screen.content.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.components.BottomNavigationBar
import com.github.backlog.ui.components.TopMenuBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.util.ViewModelContainer

abstract class MainScreen(viewModelContainer: ViewModelContainer) : BaseScreen(viewModelContainer) {

    @Composable
    protected abstract fun TopBarExtraButtons()

    @Composable
    override fun TopBar() {
        TopMenuBar(
            heading = section.resourceId,
            onMenuButtonClick = { /*TODO*/ },
            modifier = Modifier,
            extraButtons = { TopBarExtraButtons() }
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) {
        BottomNavigationBar(navController = navController, sections = sections)
    }
}
