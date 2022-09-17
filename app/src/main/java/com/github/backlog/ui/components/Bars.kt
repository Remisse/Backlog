package com.github.backlog.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.backlog.R
import com.github.backlog.Section

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Column() {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    placeholder = { Text(stringResource(R.string.searchbar_text)) },
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
                Divider()
            }

        },
        textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onBackground),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() } ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenuBar(
    @StringRes heading: Int,
    modifier: Modifier,
    extra: @Composable RowScope.() -> Unit = { }
) {
    TopAppBar(
        title = { Text(text = stringResource(heading), style = MaterialTheme.typography.headlineMedium) },
        actions = {
            extra()
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Other actions")
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubScreenTopBar(@StringRes heading: Int, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(heading), style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController, sections: List<Section>) {
    NavigationBar(
        containerColor = if (isSystemInDarkTheme()) Color.Transparent else MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        sections.forEach() { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val icon: ImageVector =
                if (selected) screen.icon else screen.iconOutlined ?: Icons.Outlined.Delete

            NavigationBarItem(
                selected = selected,
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
                icon = {
                    Icon(imageVector = icon, contentDescription = "")
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview
@Composable
fun TopMenuBarPreview() {
    Surface() {
        TopMenuBar(
            heading = R.string.nav_backlog,
            modifier = Modifier,
            extra = { }
        )
    }
}

@Preview
@Composable
fun SearchPreview() {
    Surface() {
        SearchBar(
            value = "",
            onValueChange = { },
            onSearchClick = { }
        )
    }
}
