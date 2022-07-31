package com.github.backlog.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavBackStackEntry

@Composable
fun getParentRoute(entry: State<NavBackStackEntry?>): String? {
    return entry.value?.destination?.parent?.route
}