package com.github.backlog.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.screen.BacklogScreen
import com.github.backlog.ui.screen.content.main.ProfileScreen
import com.github.backlog.ui.screen.content.main.TaskScreenContent
import com.github.backlog.ui.screen.content.main.library.LibraryScreen
import com.github.backlog.ui.screen.content.secondary.gameform.GameFormAdd
import com.github.backlog.ui.screen.content.secondary.gameform.GameFormEdit
import com.github.backlog.ui.screen.content.secondary.taskform.TaskFormAdd
import com.github.backlog.ui.screen.content.secondary.taskform.TaskFormEdit
import com.github.backlog.util.ViewModelContainerAccessor
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class NavigationState(val scaffoldState: ScaffoldState,
                      val navController: NavHostController,
                      accessor: ViewModelContainerAccessor
) {
    val mainScreens: List<BacklogScreen> = listOf(
        LibraryScreen(
            onEditCardButtonClick = { navController.navigate("${gameFormEdit.section.route}/${it}") },
            onOnlineSearchButtonClick = { /* TODO */},
            onCreateButtonClick = { navController.navigate(gameFormAdd.section.route) },
            viewModelContainer = accessor.viewModelContainer
        ),
        TaskScreenContent(
            onTaskEditClick = { navController.navigate("${taskFormEdit.section.route}/${it}") },
            onCreateClick = { navController.navigate(taskFormAdd.section.route) },
            viewModelContainer = accessor.viewModelContainer
        ),
        ProfileScreen(viewModelContainer = accessor.viewModelContainer)
    )

    val gameFormAdd = GameFormAdd(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        viewModelContainer = accessor.viewModelContainer
    )

    val gameFormEdit = GameFormEdit(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        viewModelContainer = accessor.viewModelContainer
    )

    val taskFormAdd = TaskFormAdd(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        viewModelContainer = accessor.viewModelContainer
    )

    val taskFormEdit = TaskFormEdit(
        onSuccess = { navController.navigateUp() },
        onDialogSubmitClick = { navController.navigateUp() },
        viewModelContainer = accessor.viewModelContainer
    )

    private val secondaryScreens: List<BacklogScreen> = listOf(
        gameFormAdd,
        gameFormEdit,
        taskFormAdd,
        taskFormEdit
    )

    /**
     * Returns the route name stripped of all arguments, if there are any.
     */
    private fun baseRoute(route: String): String {
        return route.split("?", "/")[0]
    }

    val startingScreen: BacklogScreen = mainScreens[0]

    /*
     * Source of truth for the currently active screen. Used to dynamically assign Composables
     * to the scaffold.
     */
    private val _currentScreen: MutableState<BacklogScreen> = mutableStateOf(startingScreen)
    private val _from: MutableState<BacklogScreen> = mutableStateOf(_currentScreen.value)

    init {
        _currentScreen.value = startingScreen

        // When navigating to a new screen, reinitialize all ViewModels.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            _from.value = _currentScreen.value
            _currentScreen.value = mainScreens.plus(secondaryScreens)
                .find { baseRoute(it.section.route) == baseRoute(destination.route.orEmpty()) }!!

            accessor.clear()
        }
    }

    fun currentScreen(): BacklogScreen {
        return _currentScreen.value
    }

    fun from(): BacklogScreen {
        return _from.value
    }

    val bottomNavBarSections: List<Section> = mainScreens.map { it.section }

    @OptIn(ExperimentalAnimationApi::class)
    fun slideDirection() : AnimatedContentScope.SlideDirection {
        if (mainScreens.indexOf(currentScreen()) > mainScreens.indexOf(from())) {
            return AnimatedContentScope.SlideDirection.Left
        }
        return AnimatedContentScope.SlideDirection.Right
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberNavigationState(scaffoldState: ScaffoldState = rememberScaffoldState(),
                            navController: NavHostController = rememberAnimatedNavController(),
                            accessor: ViewModelContainerAccessor
): NavigationState {
    return remember(scaffoldState, navController, accessor) {
        NavigationState(scaffoldState, navController, accessor)
    }
}
