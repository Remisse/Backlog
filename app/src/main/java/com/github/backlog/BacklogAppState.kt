package com.github.backlog

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.github.backlog.ui.screen.BacklogScreen
import com.github.backlog.ui.screen.content.main.LibraryScreen
import com.github.backlog.ui.screen.content.main.ProfileScreen
import com.github.backlog.ui.screen.content.main.TaskScreenContent
import com.github.backlog.ui.screen.content.secondary.gameform.GameFormAdd
import com.github.backlog.ui.screen.content.secondary.gameform.GameFormEdit
import com.github.backlog.ui.screen.content.secondary.taskform.TaskFormAdd
import com.github.backlog.ui.screen.content.secondary.taskform.TaskFormEdit
import com.github.backlog.util.AppContainer
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class BacklogAppState(val scaffoldState: ScaffoldState,
                      val navController: NavHostController,
                      appContainer: AppContainer
) {
    val main: List<BacklogScreen> = listOf(
        LibraryScreen(
            onEditCardButtonClick = { navController.navigate("${gameFormEdit.section.route}/${it}") },
            onOnlineSearchButtonClick = { /* TODO */},
            onCreateButtonClick = { navController.navigate(gameFormAdd.section.route) },
            appContainer = appContainer
        ),
        TaskScreenContent(
            onTaskEditClick = { navController.navigate("${taskFormEdit.section.route}/${it}") },
            onCreateClick = { navController.navigate(taskFormAdd.section.route) },
            appContainer = appContainer
        ),
        ProfileScreen(appContainer)
    )

    val gameFormAdd = GameFormAdd(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        appContainer = appContainer
    )

    val gameFormEdit = GameFormEdit(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        appContainer = appContainer
    )

    val taskFormAdd = TaskFormAdd(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        appContainer = appContainer
    )

    val taskFormEdit = TaskFormEdit(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        appContainer = appContainer
    )

    private val sub: List<BacklogScreen> = listOf(
        gameFormAdd,
        gameFormEdit,
        taskFormAdd,
        taskFormEdit
    )

    val startingScreen: BacklogScreen = main[0]

    private val _currentScreen: MutableState<BacklogScreen> = mutableStateOf(startingScreen)
    private val _from: MutableState<BacklogScreen> = mutableStateOf(_currentScreen.value)

    private fun baseRoute(route: String): String {
        return route.split("?", "/")[0]
    }

    init {
        _currentScreen.value = startingScreen
        navController.addOnDestinationChangedListener { _, destination, _ ->
            _from.value = _currentScreen.value
            _currentScreen.value = main.plus(sub)
                .find { baseRoute(it.section.route) == baseRoute(destination.route.orEmpty()) }!!
        }
    }

    fun currentScreen(): BacklogScreen {
        return _currentScreen.value
    }

    fun from(): BacklogScreen {
        return _from.value
    }

    val bottomNavBarSections: List<Section> = main.map { it.section }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberBacklogAppState(scaffoldState: ScaffoldState = rememberScaffoldState(),
                            navController: NavHostController = rememberAnimatedNavController(),
                            appContainer: AppContainer
): BacklogAppState {
    return remember(scaffoldState, navController, appContainer) {
        BacklogAppState(scaffoldState, navController, appContainer)
    }
}
