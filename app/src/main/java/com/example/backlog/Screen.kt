package com.example.backlog

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Profile : Screen("profile", R.string.nav_profile, Icons.Default.Face)
    object Tasks : Screen("tasks", R.string.nav_tasks, Icons.Default.Task)
    object Games : Screen("games", R.string.nav_game_list, Icons.Default.Gamepad)

    object GameCreation : Screen("game_creation", R.string.insert_topbar, Icons.Default.Add)
}