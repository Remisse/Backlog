package com.example.backlog.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.backlog.AppContainer
import com.example.backlog.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationSystem(navController: NavHostController, appContainer: AppContainer,
                     paddingValues: PaddingValues) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "main"
    ) {
        mainGraph(navController, appContainer)
        gameInsertionFormGraph(navController, appContainer)
        taskCreationFormGraph(navController, appContainer)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.mainGraph(navController: NavHostController, appContainer: AppContainer) {
    val fabModifier = Modifier.offset(y = (-64).dp)

    navigation(
        route = "main",
        startDestination = Screen.Games.route
    ) {
        composable(
            route = Screen.Games.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            exitTransition = { fadeOut() }
        ) { BacklogScreen(
            onCreateButtonClick = { navController.navigate(Screen.GameCreation.route) },
            onOnlineSearchButtonClick = { /* TODO */ },
            fabModifier = fabModifier,
            gameViewModel = viewModel(factory = appContainer.gameViewModelFactory)
        ) }
        composable(
            route = Screen.Tasks.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { fadeOut() }
        ) {
            TaskScreen(
                onCreateClick = { navController.navigate(Screen.TaskCreation.route) },
                fabModifier = fabModifier,
                taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
            )
        }
        composable(
            route = Screen.Profile.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { fadeOut() }
        ) {
            ProfileScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.gameInsertionFormGraph(navController: NavHostController, appContainer: AppContainer) {
    navigation(
        startDestination = Screen.GameCreation.route,
        route = "game_creation_parent"
    ) {
        composable(
            route = Screen.GameCreation.route,
            enterTransition = { slideInVertically() },
            exitTransition = { fadeOut() }
        ) { GameCreationScreen(
            onEntryAddSuccess = { navController.navigateUp() },
            onDialogSubmitClick = { navController.navigateUp() },
            gameViewModel = viewModel(factory = appContainer.gameViewModelFactory)
        ) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.taskCreationFormGraph(navController: NavHostController, appContainer: AppContainer) {
    navigation(
        startDestination = Screen.TaskCreation.route,
        route = "task_creation_parent"
    ) {
        composable(
            route = Screen.TaskCreation.route,
            enterTransition = { slideInVertically() },
            exitTransition = { fadeOut() }
        ) { TaskCreationScreen(
            onEntryAddSuccess = { navController.navigateUp() },
            onDialogSubmitClick = { navController.navigateUp() },
            gameViewModel = viewModel(factory = appContainer.gameViewModelFactory),
            taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
        ) }
    }
}
