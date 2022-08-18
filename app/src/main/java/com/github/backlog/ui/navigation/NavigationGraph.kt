package com.github.backlog.ui.navigation

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.navigation
import com.github.backlog.ui.screen.BacklogScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

private const val root = "main"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationRoot(navState: NavigationState) {
    Scaffold(
        scaffoldState = navState.scaffoldState,
        topBar = { navState.currentScreen().TopBar() },
        bottomBar = {
            navState
                .currentScreen()
                .BottomBar(navState.navController, navState.bottomNavBarSections)
        },
        floatingActionButton = { navState.currentScreen().Fab() },
    ) {
        AnimatedNavHost(
            navController = navState.navController,
            startDestination = root,
            modifier = Modifier.padding(it),
            contentAlignment = Alignment.TopCenter
        ) {
            mainGraph(navState)

            basicSecondaryGraph(navState.gameFormAdd)
            secondaryWithNonNullArgumentGraph(navState.gameFormEdit, "gameId", NavType.IntType)

            basicSecondaryGraph(navState.taskFormAdd)
            secondaryWithNonNullArgumentGraph(navState.taskFormEdit, "taskId", NavType.IntType)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.mainGraph(appState: NavigationState) {
    navigation(
        route = root,
        startDestination = appState.startingScreen.section.route
    ) {
        appState.mainScreens.forEach { screen ->
            composable(
                route = screen.section.route,
                enterTransition = { slideIntoContainer(appState.slideDirection(), spring()) },
                exitTransition = { slideOutOfContainer(appState.slideDirection(), spring()) }
            ) {
                screen.Content(null)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.basicSecondaryGraph(screen: BacklogScreen) {
    composable(
        route = screen.section.route,
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { -it } }
    ) {
        screen.Content(null)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.secondaryWithNonNullArgumentGraph(screen: BacklogScreen,
                                                              argKey: String,
                                                              argType: NavType<*>
) {
    composable(
        route = "${screen.section.route}/{$argKey}",
        arguments = listOf(navArgument(argKey) { type = argType }),
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { -it } }
    ) { from ->
        screen.Content(Bundle(from.arguments))
    }
}
