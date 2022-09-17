package com.github.backlog.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.github.backlog.Section
import com.github.backlog.ui.screen.BacklogScreen
import com.github.backlog.ui.screen.main.library.LibraryScreen
import com.github.backlog.ui.screen.main.profile.ProfileScreen
import com.github.backlog.ui.screen.main.tasks.TaskScreen
import com.github.backlog.ui.screen.secondary.gameform.GameFormAdd
import com.github.backlog.ui.screen.secondary.gameform.GameFormEdit
import com.github.backlog.ui.screen.secondary.library.QuickGameDeleteScreen
import com.github.backlog.ui.screen.secondary.library.QuickStatusChangeDialogScreen
import com.github.backlog.ui.screen.secondary.onlinesearch.GameFormOnlineImport
import com.github.backlog.ui.screen.secondary.onlinesearch.OnlineSearchScreen
import com.github.backlog.ui.screen.secondary.steam.SteamDialogScreen
import com.github.backlog.ui.screen.secondary.steam.SteamImportScreen
import com.github.backlog.ui.screen.secondary.taskform.TaskFormAdd
import com.github.backlog.ui.screen.secondary.taskform.TaskFormEdit
import com.github.backlog.utils.ViewModelFactoryStore
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

val START_ROUTE: String = Section.Library.route

@Stable
class NavigationState(
    val navController: NavHostController,
    vmFactories: ViewModelFactoryStore
) {
    private val _navigateUp: () -> Unit = { navController.navigateUp() }

    val mainScreens: List<BacklogScreen> = listOf(
        LibraryScreen(
            onEditCardButtonClick = { navController.navigate("${Section.GameEdit.route}/$it") },
            onOnlineSearchButtonClick = { navController.navigate(Section.OnlineSearch.route) },
            onCreateButtonClick = { navController.navigate(Section.GameAdd.route) },
            onSteamImportClick = { navController.navigate(Section.SteamImportPrep.route) },
            onStatusChangeClick = { navController.navigate("${Section.QuickGameStatusChange.route}/$it") },
            onDeleteClick = { navController.navigate("${Section.QuickGameDelete.route}/$it") },
            vmFactories = vmFactories
        ),
        TaskScreen(
            onTaskEditClick = { navController.navigate("${Section.TaskEdit.route}/$it") },
            onCreateClick = { navController.navigate(Section.TaskAdd.route) },
            vmFactories = vmFactories
        ),
        ProfileScreen(
            vmFactories = vmFactories
        )
    )

    val secondaryScreens: List<BacklogScreen> = listOf(
        GameFormAdd(
            onSuccess = _navigateUp,
            onCancelDialogConfirm = _navigateUp,
            vmFactories = vmFactories
        ),
        TaskFormAdd(
            onSuccess = _navigateUp,
            onDialogSubmitClick = _navigateUp,
            vmFactories = vmFactories
        ),
        OnlineSearchScreen(
            onBackClick = _navigateUp,
            onGameClick = { navController.navigate("${Section.OnlineImport.route}/$it") },
            vmFactories = vmFactories
        )
    )

    val secondaryWithArgument: Map<BacklogScreen, Pair<String, NavType<*>>> = mapOf(
        GameFormEdit(
            onSuccess = _navigateUp,
            onCancelDialogConfirm = _navigateUp,
            vmFactories = vmFactories
        ) to Pair("gameId", NavType.IntType),
        TaskFormEdit(
            onSuccess = _navigateUp,
            onDialogSubmitClick = _navigateUp,
            vmFactories = vmFactories
        ) to Pair("taskId",NavType.IntType),
        GameFormOnlineImport(
            onCancelDialogConfirm = _navigateUp,
            onSuccess = _navigateUp,
            vmFactories = vmFactories,
            onNetworkErrorAcknowledge = _navigateUp
        ) to Pair("gameId", NavType.StringType),
        SteamImportScreen(
            onBackClick = _navigateUp,
            onSuccess = _navigateUp,
            onNetworkErrorAcknowledge = _navigateUp,
            vmFactories = vmFactories
        ) to Pair("steamId", NavType.StringType)
    )

    val dialogs: List<BacklogScreen> = listOf(
        SteamDialogScreen(
            onDismissRequest = _navigateUp,
            onConfirmClick = { navController.navigate("${Section.SteamImport.route}/$it") },
            vmFactories = vmFactories
        )
    )

    val dialogsWithArgument: Map<BacklogScreen, Pair<String, NavType<*>>> = mapOf(
        QuickStatusChangeDialogScreen(
            onDismissRequest = _navigateUp,
            onStatusSelect = _navigateUp,
            vmFactories = vmFactories
        ) to Pair("gameId", NavType.IntType),
        QuickGameDeleteScreen(
            onDismissRequest = _navigateUp,
            onConfirmClick = _navigateUp,
            vmFactories = vmFactories
        ) to Pair("gameId", NavType.IntType)
    )

    /**
     * Returns the route name stripped of all arguments, if there are any.
     */
    private fun baseRoute(route: String): String {
        return route.split("?", "/")[0]
    }

    /**
     * Source of truth for the currently active screen. Used to dynamically assign Composables
     * to the scaffold.
     */
    private var _currentScreen: MutableState<BacklogScreen>

    /**
     * Previously active screen.
     */
    private var _from: BacklogScreen

    private val _allScreens = mainScreens +
            secondaryScreens +
            secondaryWithArgument.keys +
            dialogs +
            dialogsWithArgument.keys

    init {
        _currentScreen =
            mutableStateOf(_allScreens.find { baseRoute(START_ROUTE) == baseRoute(it.section.route) }!! )
        _from = _currentScreen.value

        /*
         * When navigating to a new screen, update the current one and clear the VMs of the
         * previous screen.
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            _from = _currentScreen.value
            _currentScreen.value = _allScreens.find {
                baseRoute(it.section.route) == baseRoute(destination.route.orEmpty())
            }!!

            // Prevent an unwanted clear at startup.
            if (_currentScreen.value != _from) {
                _from.viewModelStoreOwner.viewModelStore.clear()
            }
        }
    }

    fun currentScreen(): BacklogScreen {
        return _currentScreen.value
    }

    val bottomNavBarSections: List<Section> = mainScreens.map { it.section }

    @OptIn(ExperimentalAnimationApi::class)
    fun slideDirection() : AnimatedContentScope.SlideDirection {
        if (mainScreens.indexOf(_currentScreen.value) > mainScreens.indexOf(_from)) {
            return AnimatedContentScope.SlideDirection.Left
        }
        return AnimatedContentScope.SlideDirection.Right
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberAnimatedNavController(),
    vmFactories: ViewModelFactoryStore
): NavigationState {
    return remember(navController, vmFactories) {
        NavigationState(navController, vmFactories)
    }
}
