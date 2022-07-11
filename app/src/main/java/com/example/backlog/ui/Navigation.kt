package com.example.backlog.ui

import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.backlog.Screen
import com.example.backlog.ui.form.GameEditScreen
import com.example.backlog.ui.form.GameInsertScreen
import com.example.backlog.ui.form.TaskEditScreen
import com.example.backlog.ui.form.TaskInsertScreen
import com.example.backlog.util.AppContainer
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationSystem(navController: NavHostController, appContainer: AppContainer,
                     paddingValues: PaddingValues, startDestination: String) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ) {
        mainGraph(navController, appContainer)

        gameInsertionFormGraph(navController, appContainer)
        gameEditFormGraph(navController, appContainer)

        taskCreationFormGraph(navController, appContainer)
        taskEditFormGraph(navController, appContainer)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.mainGraph(navController: NavHostController, appContainer: AppContainer) {
    navigation(
        route = "main",
        startDestination = Screen.Games.route
    ) {
        composable(
            route = Screen.Games.route,
            enterTransition = { slideIntoContainer(Right, tween()) },
            exitTransition = { slideOutOfContainer(Left, tween()) }
        ) {
            BacklogScreen(
                onEditCardClick = { navController.navigate("${Screen.GameEdit.route}/${it}") },
                gameViewModel = viewModel(factory = appContainer.gameViewModelFactory),
            )
        }
        composable(
            route = Screen.Tasks.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Games.route -> slideIntoContainer(Left, tween())
                    Screen.Profile.route -> slideIntoContainer(Right, tween())
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Games.route -> slideOutOfContainer(Right, tween())
                    Screen.Profile.route -> slideOutOfContainer(Left, tween())
                    else -> null
                }
            }
        ) {
            TaskScreen(
                onTaskEditClick = { navController.navigate("${Screen.TaskEdit.route}/${it}") },
                taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
            )
        }
        composable(
            route = Screen.Profile.route,
            enterTransition = { slideIntoContainer(Left, tween()) },
            exitTransition = { slideOutOfContainer(Right, tween()) }
        ) {
            ProfileScreen(
                gameViewModel = viewModel(factory = appContainer.gameViewModelFactory),
                taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
            )
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
            enterTransition = { slideInVertically { it } },
            exitTransition = { slideOutVertically { -it } }
        ) { GameInsertScreen(
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
            enterTransition = { slideInVertically { it } },
            exitTransition = { slideOutVertically { -it } }
        ) { TaskInsertScreen(
                onEntryAddSuccess = { navController.navigateUp() },
                onCancelDialogSubmit = { navController.navigateUp() },
                gameViewModel = viewModel(factory = appContainer.gameViewModelFactory),
                taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
        ) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.gameEditFormGraph(navController: NavHostController, appContainer: AppContainer) {
    navigation(
        startDestination = "${Screen.GameEdit.route}/{gameId}",
        route = "game_edit_parent"
    ) {
        composable(
            route = "${Screen.GameEdit.route}/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.IntType }),
            enterTransition = { slideInVertically { it } },
            exitTransition = { slideOutVertically { -it } }
        ) { backStackEntry ->
            GameEditScreen(
                gameId = backStackEntry.arguments?.getInt("gameId")!!,
                onEditSuccess = { navController.navigateUp() },
                onCancelDialogSubmitClick = { navController.navigateUp() },
                gameViewModel = viewModel(factory = appContainer.gameViewModelFactory)
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.taskEditFormGraph(navController: NavHostController, appContainer: AppContainer) {
    navigation(
        startDestination = "${Screen.TaskEdit.route}/{taskId}",
        route = "task_edit_parent"
    ) {
        composable(
            route = "${Screen.TaskEdit.route}/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
            enterTransition = { slideInVertically { it } },
            exitTransition = { slideOutVertically { -it } }
        ) { backStackEntry ->
            TaskEditScreen(
                taskId = backStackEntry.arguments?.getInt("taskId")!!,
                onEditSuccess = { navController.navigateUp() },
                onCancelDialogSubmitClick = { navController.navigateUp() },
                gameViewModel = viewModel(factory = appContainer.gameViewModelFactory),
                taskViewModel = viewModel(factory = appContainer.taskViewModelFactory)
            )
        }
    }
}
