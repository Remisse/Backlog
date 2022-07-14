package com.example.backlog.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.backlog.Screen

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit, modifier: Modifier, shape: Shape) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
        label = {
            Row() {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Text("Search")
            }
        },
        shape = shape
    )
}

@Composable
fun TopMenuBar(@StringRes heading: Int, onMenuButtonClick: () -> Unit, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onMenuButtonClick()
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
            Text(text = stringResource(heading), style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun SubScreenTopBar(@StringRes heading: Int, onClick: () -> Unit) {
    TopAppBar() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClick ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = stringResource(heading), style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, screens: List<Screen>) {
    BottomAppBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach() { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                label = { Text(stringResource(screen.resourceId)) },
                icon = { Icon(imageVector = screen.icon, contentDescription = null) }
            )
        }
    }
}