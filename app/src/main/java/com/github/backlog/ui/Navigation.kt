package com.github.backlog.ui

import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.navigation
import com.github.backlog.BacklogAppState
import com.github.backlog.ui.screen.BacklogScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

private const val root = "main"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationRoot(appState: BacklogAppState) {
    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = { appState.currentScreen().TopBar() },
        bottomBar = { appState.currentScreen().BottomBar(appState.navController, appState.bottomNavBarSections) },
        floatingActionButton = { appState.currentScreen().Fab() }
    ) {
        AnimatedNavHost(
            navController = appState.navController,
            startDestination = root,
            modifier = Modifier.padding(it),
            contentAlignment = Alignment.TopCenter
        ) {
            mainGraph(appState.main, appState.startingScreen)

            basicSecondaryGraph(appState.gameFormAdd)
            secondaryWithIntArgumentGraph(appState.gameFormEdit, "gameId")

            basicSecondaryGraph(appState.taskFormAdd)
            secondaryWithIntArgumentGraph(appState.taskFormEdit, "taskId")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.mainGraph(screens: List<BacklogScreen>, start: BacklogScreen) {
    navigation(
        route = root,
        startDestination = start.section.route
    ) {
        screens.forEach { screen ->
            composable(
                route = screen.section.route,
                enterTransition = { slideIntoContainer(Right, tween()) },
                exitTransition = { slideOutOfContainer(Left, tween()) }
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
private fun NavGraphBuilder.secondaryWithIntArgumentGraph(screen: BacklogScreen, argKey: String) {
    composable(
        route = "${screen.section.route}/{$argKey}",
        arguments = listOf(navArgument(argKey) { type = NavType.IntType }),
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { -it } }
    ) { from ->
        val args = Bundle()
        from.arguments?.getInt(argKey)
            ?.let { args.putInt(argKey, it) }
        screen.Content(args)
    }
}
