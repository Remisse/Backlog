package com.github.backlog

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Section(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    // Main
    object Profile : Section("profile", R.string.nav_profile, Icons.Default.Face)
    object Tasks : Section("tasks", R.string.nav_tasks, Icons.Default.Task)
    object Library : Section("games", R.string.nav_backlog, Icons.Default.Gamepad)

    // Secondary
    object GameAdd : Section("game_add", R.string.game_form_heading, Icons.Default.Add)
    object GameEdit : Section("game_edit", R.string.game_form_heading, Icons.Default.Edit)

    object TaskAdd : Section("task_add", R.string.task_fab_add, Icons.Default.Add)
    object TaskEdit : Section("task_edit", R.string.task_update_heading, Icons.Default.EditNote)
    
    object OnlineSearch : Section("online_search", R.string.online_search_title, Icons.Default.Web)
    object OnlineImport : Section("online_import", R.string.import_game_heading, Icons.Default.Edit)
    
    object SteamImportPrep : Section("steam_import_prep", R.string.steam_import_prep_heading, Icons.Default.Edit)
    object SteamImport : Section("steam_import", R.string.steam_import_heading, Icons.Default.List)
}
