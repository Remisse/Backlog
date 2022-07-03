package com.example.backlog.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.backlog.AppContainer
import com.example.backlog.ui.theme.BacklogTheme

@Composable
fun TasksScreen(navController: NavHostController) {
    BacklogTheme() {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {

        }
    }
}