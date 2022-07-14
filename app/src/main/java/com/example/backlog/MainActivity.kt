package com.example.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.backlog.ui.*
import com.example.backlog.ui.common.BottomNavigationBar
import com.example.backlog.ui.common.TopMenuBar
import com.example.backlog.ui.common.getParentRoute
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.util.AppContainer
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

private const val MAIN_DESTINATION = "main"

class MainActivity : ComponentActivity() {

    private lateinit var container: AppContainer

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        container = (application as BacklogApplication).appContainer
        setContent {
            val scaffoldState = rememberScaffoldState()
            val navController = rememberAnimatedNavController()

            val screens = listOf(Screen.Games, Screen.Tasks, Screen.Profile)
            val startingDestination = MAIN_DESTINATION

            val showNavbar = getParentRoute(entry = navController.currentBackStackEntryAsState()) == MAIN_DESTINATION

            BacklogTheme() {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        if (showNavbar) {
                            val header = screens.find { it.route == navController.currentDestination?.route }
                                ?.resourceId ?: R.string.app_name
                            TopMenuBar(
                                heading = header,
                                onMenuButtonClick = { /*TODO*/ },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    bottomBar = {
                        if (showNavbar) {
                            BottomNavigationBar(navController = navController, screens = screens)
                        }
                    },
                    floatingActionButton = {
                        when (navController.currentDestination?.route) {
                            Screen.Games.route -> BacklogFab(
                                onCreateButtonClick = { navController.navigate(Screen.GameCreation.route) },
                                onOnlineSearchButtonClick = { /*TODO*/ }
                            )
                            Screen.Tasks.route -> TaskFab(onCreateClick = {
                                navController.navigate(Screen.TaskCreation.route)
                            })
                        }
                    }
                ) { padding ->
                    NavigationSystem(navController, container, padding, startingDestination)
                }
            }
        }
    }
}
