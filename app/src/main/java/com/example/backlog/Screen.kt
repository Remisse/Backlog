package com.example.backlog

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Profile : Screen("profile", R.string.nav_profile, Icons.Default.Face)
    object Tasks : Screen("tasks", R.string.nav_tasks, Icons.Default.Task)
    object Games : Screen("games", R.string.nav_backlog, Icons.Default.Gamepad)

    object GameCreation : Screen("game_creation", R.string.insert_topbar, Icons.Default.Add)
    object GameEdit : Screen("game_edit", R.string.game_edit_heading, Icons.Default.EditNote)

    object TaskCreation : Screen("task_creation", R.string.task_fab_add, Icons.Default.Add)
    object TaskEdit : Screen("task_edit", R.string.task_update_heading, Icons.Default.EditNote)
}
