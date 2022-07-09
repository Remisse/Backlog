package com.example.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.backlog.ui.BottomNavigationBar
import com.example.backlog.ui.NavigationSystem
import com.example.backlog.ui.TopMenuBar
import com.example.backlog.ui.getParentRoute
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.util.AppContainer
import com.example.backlog.util.AppContainerImpl
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {

    private lateinit var container: AppContainer

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        container = (application as BacklogApplication).appContainer
        setContent {
            val screens = listOf(Screen.Games, Screen.Tasks, Screen.Profile)
            val navController = rememberAnimatedNavController()
            val startingDestination = "main"

            val showNavbar = getParentRoute(entry = navController.currentBackStackEntryAsState()) == "main"

            BacklogTheme() {
                Scaffold(
                    topBar = {
                        if (showNavbar) {
                            val header = screens.find { it.route == navController.currentDestination?.route }
                                ?.resourceId ?: R.string.app_name
                            TopMenuBar(header = header, onMenuButtonClick = { /*TODO*/ },
                                modifier = Modifier.fillMaxWidth())
                        }
                    },
                    bottomBar = { 
                        if (showNavbar) {
                            BottomNavigationBar(navController = navController, screens = screens)
                        }
                    }
                ) { padding ->
                    NavigationSystem(navController, container, padding, startingDestination)
                }
            }
        }
    }
}
