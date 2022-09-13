package com.github.backlog.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.backlog.R
import com.github.backlog.Section

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(.8f)
            .padding(vertical = 4.dp)
            .onFocusChanged { focused = it.isFocused },
        decorationBox = { textField ->
            Box(modifier = modifier) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    ) {
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        if (!focused) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.alt_search)
                            )
                            Text(
                                text = stringResource(R.string.searchbar_text),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            textField()
                        }
                    }
                    Divider(color = MaterialTheme.colors.secondary)
                }
            }
        },
        textStyle = TextStyle.Default.copy(color = MaterialTheme.colors.onBackground),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() } ),
        cursorBrush = SolidColor(MaterialTheme.colors.primary)
    )
}

@Composable
fun TopMenuBar(
    @StringRes heading: Int,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier,
    extra: @Composable RowScope.() -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.animateContentSize()
        ) {
            IconButton(onClick = onMenuButtonClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.alt_side_menu)
                )
            }
            Text(text = stringResource(heading), style = MaterialTheme.typography.h6)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .animateContentSize()
                .weight(1f)
        ) {
            extra()
        }
    }
}

@Composable
fun SubScreenTopBar(@StringRes heading: Int, onBackClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
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
    BottomAppBar(
        backgroundColor = if (isSystemInDarkTheme()) Color.Transparent else lightColors().primary
    ) {
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
                icon = { Icon(imageVector = screen.icon, contentDescription = "") }
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
            onMenuButtonClick = { /*TODO*/ },
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
