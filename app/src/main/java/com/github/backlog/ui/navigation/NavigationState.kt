package com.github.backlog.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.github.backlog.Section
import com.github.backlog.ui.screen.BacklogScreen
import com.github.backlog.ui.screen.secondary.steam.SteamDialogScreen
import com.github.backlog.ui.screen.main.profile.ProfileScreen
import com.github.backlog.ui.screen.main.library.LibraryScreen
import com.github.backlog.ui.screen.main.tasks.TaskScreen
import com.github.backlog.ui.screen.main.tasks.TaskScreenContent
import com.github.backlog.ui.screen.secondary.gameform.GameFormAdd
import com.github.backlog.ui.screen.secondary.gameform.GameFormEdit
import com.github.backlog.ui.screen.secondary.onlinesearch.GameFormOnlineImport
import com.github.backlog.ui.screen.secondary.onlinesearch.OnlineSearchScreen
import com.github.backlog.ui.screen.secondary.steam.SteamImportScreen
import com.github.backlog.ui.screen.secondary.taskform.TaskFormAdd
import com.github.backlog.ui.screen.secondary.taskform.TaskFormEdit
import com.github.backlog.utils.ViewModelContainerAccessor
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NavigationState(val scaffoldState: ScaffoldState,
                      val navController: NavHostController,
                      accessor: ViewModelContainerAccessor
) {
    private val _navigateUp: () -> Unit = { navController.navigateUp() }

    val mainScreens: List<BacklogScreen> = listOf(
        LibraryScreen(
            onEditCardButtonClick = { navController.navigate("${Section.GameEdit.route}/$it") },
            onOnlineSearchButtonClick = { navController.navigate(Section.OnlineSearch.route) },
            onCreateButtonClick = { navController.navigate(Section.GameAdd.route) },
            onSteamImportClick = { navController.navigate(Section.SteamImportPrep.route) },
            drawerState = scaffoldState.drawerState,
            accessor = accessor
        ),
        TaskScreen(
            onTaskEditClick = { navController.navigate("${Section.TaskEdit.route}/$it") },
            onCreateClick = { navController.navigate(Section.TaskAdd.route) },
            drawerState = scaffoldState.drawerState,
            accessor = accessor
        ),
        ProfileScreen(
            drawerState = scaffoldState.drawerState,
            accessor = accessor
        )
    )

    val secondaryScreens: List<BacklogScreen> = listOf(
        GameFormAdd(
            onSuccess = _navigateUp,
            onCancelDialogConfirm = _navigateUp,
            accessor = accessor
        ),
        TaskFormAdd(
            onSuccess = _navigateUp,
            onDialogSubmitClick = _navigateUp,
            accessor = accessor
        ),
        OnlineSearchScreen(
            onBackClick = _navigateUp,
            onGameClick = { navController.navigate("${Section.OnlineImport.route}/$it") },
            accessor = accessor
        )
    )

    val secondaryWithArgument: Map<BacklogScreen, Pair<String, NavType<*>>> = mapOf(
        GameFormEdit(
            onSuccess = _navigateUp,
            onCancelDialogConfirm = _navigateUp,
            accessor = accessor
        ) to Pair("gameId", NavType.IntType),
        TaskFormEdit(
            onSuccess = _navigateUp,
            onDialogSubmitClick = _navigateUp,
            accessor = accessor
        ) to Pair("taskId",NavType.IntType),
        GameFormOnlineImport(
            onCancelDialogConfirm = _navigateUp,
            onSuccess = _navigateUp,
            accessor = accessor,
            onNetworkErrorAcknowledge = _navigateUp
        ) to Pair("gameId", NavType.StringType),
        SteamImportScreen(
            onBackClick = _navigateUp,
            onSuccess = _navigateUp,
            onNetworkErrorAcknowledge = _navigateUp,
            accessor = accessor
        ) to Pair("steamId", NavType.StringType)
    )

    val dialogs: List<BacklogScreen> = listOf(
        SteamDialogScreen(
            onDismissRequest = _navigateUp,
            onConfirmClick = { navController.navigate("${Section.SteamImport.route}/$it") },
            accessor = accessor
        )
    )

    /**
     * Returns the route name stripped of all arguments, if there are any.
     */
    private fun baseRoute(route: String): String {
        return route.split("?", "/")[0]
    }

    val startingScreen: BacklogScreen = mainScreens[0]

    /**
     * Source of truth for the currently active screen. Used to dynamically assign Composables
     * to the scaffold.
     */
    private val _currentScreen: MutableState<BacklogScreen> = mutableStateOf(startingScreen)

    /**
     * Previously active screen.
     */
    private val _from: MutableState<BacklogScreen> = mutableStateOf(_currentScreen.value)

    private val _allScreens = mainScreens.plus(secondaryScreens)
        .plus(secondaryWithArgument.keys)
        .plus(dialogs)

    init {
        _currentScreen.value = startingScreen

        // When navigating to a new screen, update the current screen and reinitialize all ViewModels.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            _from.value = _currentScreen.value
            _currentScreen.value = _allScreens.find {
                baseRoute(it.section.route) == baseRoute(destination.route.orEmpty())
            }!!

            accessor.clear()
        }
    }

    fun currentScreen(): BacklogScreen {
        return _currentScreen.value
    }

    val bottomNavBarSections: List<Section> = mainScreens.map { it.section }

    @OptIn(ExperimentalAnimationApi::class)
    fun slideDirection() : AnimatedContentScope.SlideDirection {
        if (mainScreens.indexOf(_currentScreen.value) > mainScreens.indexOf(_from.value)) {
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
