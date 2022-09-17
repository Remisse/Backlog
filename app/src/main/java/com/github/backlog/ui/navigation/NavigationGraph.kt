package com.github.backlog.ui.navigation

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.github.backlog.Section
import com.github.backlog.ui.screen.BacklogScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

private const val ROOT = "main"

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(navState: NavigationState) {
    Scaffold(
        topBar = {
            navState.currentScreen()
                .TopBar()
        },
        bottomBar = {
            navState.currentScreen()
                .BottomBar(navState.navController, navState.bottomNavBarSections)
        },
        floatingActionButton = {
            navState.currentScreen()
                .Fab()
        },
    ) { padding ->
        AnimatedNavHost(
            navController = navState.navController,
            startDestination = ROOT,
            modifier = Modifier.padding(padding),
            contentAlignment = Alignment.TopStart // Removing this will make animations look weird
        ) {
            mainGraph(navState)

            navState.secondaryScreens.forEach {
                basicSecondaryGraph(it)
            }

            navState.secondaryWithArgument.forEach { (screen, pair) ->
                secondaryWithNonNullArgumentGraph(screen, pair.first, pair.second)
            }

            navState.dialogs.forEach { screen ->
                dialog(screen.section.route) {
                    screen.Content(null)
                }
            }

            navState.dialogsWithArgument.forEach { (screen, pair) ->
                dialogWithNonNullArgumentGraph(screen, pair.first, pair.second)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.mainGraph(appState: NavigationState) {
    navigation(
        route = ROOT,
        startDestination = START_ROUTE
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
private fun NavGraphBuilder.secondaryWithNonNullArgumentGraph(
    screen: BacklogScreen,
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

private fun NavGraphBuilder.dialogWithNonNullArgumentGraph(
    screen: BacklogScreen,
    argKey: String,
    argType: NavType<*>
) {
    dialog(
        route = "${screen.section.route}/{$argKey}",
        arguments = listOf(navArgument(argKey) { type = argType })
    ) { from ->
        screen.Content(Bundle(from.arguments))
    }
}
