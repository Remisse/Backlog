package com.github.backlog.ui.screen.main

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

abstract class MainScreen(vmFactories: ViewModelFactoryStore) : BaseScreen(vmFactories) {

    @Composable
    protected abstract fun TopBarExtraButtons()

    @Composable
    override fun TopBar() {
        TopMenuBar(
            heading = section.resourceId,
            modifier = Modifier,
            extra = { TopBarExtraButtons() }
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) {
        BottomNavigationBar(navController = navController, sections = sections)
    }
}
