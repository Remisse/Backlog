package com.example.backlog

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Profile : Screen("profile", R.string.nav_profile, Icons.Default.Face)
    object Tasks : Screen("tasks", R.string.nav_tasks, Icons.Default.Task)
    object Games : Screen("games", R.string.nav_game_list, Icons.Default.Gamepad)

    object GameCreation : Screen("game_creation", R.string.insert_topbar, Icons.Default.Add)

    object TaskCreation : Screen("task_creation", R.string.task_fab_add, Icons.Default.Add)
}
