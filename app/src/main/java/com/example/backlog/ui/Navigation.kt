package com.example.backlog.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.backlog.AppContainer
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.Screen
import com.example.backlog.viewmodel.GameViewModel

@Composable
fun NavigationSystem(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Games.route) {
        composable(Screen.Games.route) { BacklogScreen(navController,
            viewModel(factory = appContainer.gameViewModelFactory)) }
        composable(Screen.Tasks.route) { TasksScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }

        composable(Screen.GameCreation.route) { GameCreationScreen(navController,
            viewModel(factory = appContainer.gameViewModelFactory)) }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(Screen.Games, Screen.Tasks, Screen.Profile)

    BacklogTheme() {
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            screens.forEach() { screen ->
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    label = { Text(stringResource(screen.resourceId)) },
                    icon = { Icon(imageVector = screen.icon, contentDescription = null) }
                )
            }
        }
    }
}
