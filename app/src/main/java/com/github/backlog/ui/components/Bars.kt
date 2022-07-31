package com.github.backlog.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.backlog.R
import com.github.backlog.Section

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
fun TopMenuBar(@StringRes heading: Int, onMenuButtonClick: () -> Unit, modifier: Modifier,
               extraButtons: @Composable () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuButtonClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
            Text(text = stringResource(heading), style = MaterialTheme.typography.h6)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            extraButtons()
        }
    }
}

@Composable
fun SubScreenTopBar(@StringRes heading: Int, onBackClick: () -> Unit) {
    TopAppBar() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = stringResource(heading), style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, sections: List<Section>) {
    BottomAppBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        sections.forEach() { screen ->
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

@Preview
@Composable
fun TopPreview() {
    Surface() {
        TopMenuBar(
            heading = R.string.nav_backlog,
            onMenuButtonClick = { /*TODO*/ },
            modifier = Modifier,
            extraButtons = { IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.FilterAlt, contentDescription = null)
            } }
        )
    }
}
