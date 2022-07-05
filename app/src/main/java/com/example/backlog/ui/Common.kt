package com.example.backlog.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.backlog.Screen

@Composable
fun ItemCard(topText: @Composable () -> Unit, subText: List<@Composable () -> Unit>,
             hiddenText: List<@Composable () -> Unit>) {
    val isClicked = remember { mutableStateOf(false) }
    val buttonIcon = if (!isClicked.value) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp

    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                topText()
                subText.forEach { it() }
                if (isClicked.value) {
                    hiddenText.forEach { it() }
                }
            }
            IconButton(
                onClick = { isClicked.value = !isClicked.value }
            ) {
                Icon(imageVector = buttonIcon, contentDescription = null)
            }
        }
    }
}

@Composable
fun FormField(@StringRes res: Int, valueMap: SnapshotStateMap<Int, String>, modifier: Modifier, shape: Shape) {
    valueMap.putIfAbsent(res, "")

    OutlinedTextField(
        modifier = modifier,
        value = valueMap[res].orEmpty(),
        onValueChange = { valueMap[res] = it },
        label = { Text(stringResource(res)) },
        shape = shape
    )
}

@Composable
fun FieldColumn(valueMap: SnapshotStateMap<Int, String>, fieldsRes: List<Int>,
                modifier: Modifier, fieldShape: Shape, fieldModifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        fieldsRes.forEach { FormField(res = it, valueMap = valueMap, fieldModifier, fieldShape) }
    }
}

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
fun TopMenuBar(@StringRes header: Int, onMenuButtonClick: () -> Unit, modifier: Modifier) {
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
            Text(text = stringResource(header), style = MaterialTheme.typography.h6)
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
    BottomNavigation() {
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

@Composable
fun ActionsFab(@StringRes textRes: Int, icon: ImageVector, modifier: Modifier,
               miniFabs: @Composable () -> Unit) {
    val isClicked: MutableState<Boolean> = remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isClicked.value, label = "FabClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else -45f }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = isClicked.value,
            enter = slideInHorizontally(initialOffsetX = { it * 3}),
            exit = slideOutHorizontally(targetOffsetX = { it * 4})
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                miniFabs()
            }
        }
        ExtendedFloatingActionButton(
            text = { Text(stringResource(textRes).uppercase()) } ,
            onClick = { isClicked.value = !isClicked.value },
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation.value)
                )
            },
        )
    }
}

@Composable
fun CancelDialog(onDismissRequest: () -> Unit, dialogContents: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(8.dp)) {
            dialogContents()
        }
    }
}

@Composable
fun CancelDialogContent(@StringRes heading: Int, @StringRes description: Int, @StringRes stayRes: Int,
                        @StringRes returnRes: Int, modifier: Modifier, onStayButtonClick: () -> Unit,
                        onSubmitButtonClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(heading),
            style = MaterialTheme.typography.h6.plus(TextStyle(fontWeight = FontWeight.Bold)),
            modifier = modifier
        )
        Text(
            text = stringResource(description),
            textAlign = TextAlign.Start,
            modifier = modifier
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
            Button(
                onClick = onStayButtonClick,
                colors = ButtonDefaults.outlinedButtonColors()) {
                Text(stringResource(stayRes).uppercase())
            }
            Button(onClick = onSubmitButtonClick) {
                Text(stringResource(returnRes).uppercase())
            }
        }
    }
}
