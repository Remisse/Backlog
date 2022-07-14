package com.example.backlog.ui.common

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.backlog.R
import com.example.backlog.ui.theme.Teal200

@Composable
fun TextIcon(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    textColor: Color = Color.Unspecified,
    imageVector: ImageVector,
    iconTint: Color = Color.Unspecified,
    iconModifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = imageVector, contentDescription = null, tint = iconTint, modifier = iconModifier)
        Text(text = text, style = style, color = textColor)
    }
}

@Composable
fun CardSubtitleTextIcon(text: String, imageVector: ImageVector) {
    TextIcon(
        text = text,
        imageVector = imageVector,
        style = MaterialTheme.typography.caption,
        textColor = LocalContentColor.current.copy(alpha = 0.75f),
        iconTint = Teal200,
        iconModifier = Modifier.size(16.dp)
    )
}

@Composable
fun ItemCard(modifier: Modifier, exposedText: @Composable () -> Unit, hiddenText: (@Composable () -> Unit)?,
             onChangeStatusClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var showActionMenu by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isExpanded, label = "ExpandClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 180f }

    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        Surface(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.animateContentSize()
                .fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1.0f, true)) {
                        exposedText()
                    }
                    Column(modifier = Modifier.weight(0.375f, false)) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { showActionMenu = !showActionMenu }) {
                                Icon(
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = null
                                )
                            }
                            if (hiddenText != null) {
                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        modifier = Modifier.rotate(rotation.value)
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showActionMenu,
                                onDismissRequest = { showActionMenu = false }
                            ) {
                                DropdownMenuItem(onClick = {
                                    showActionMenu = false
                                    onChangeStatusClick()
                                }) {
                                    Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    Text(stringResource(R.string.change_status_button))
                                }
                                DropdownMenuItem(onClick = {
                                    showActionMenu = false
                                    onEditClick()
                                }) {
                                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    Text(stringResource(R.string.edit_button))
                                }
                                Divider()
                                DropdownMenuItem(onClick = {
                                    showActionMenu = false
                                    onDeleteClick()
                                }) {
                                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = Color.Red)
                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    Text(text = stringResource(R.string.card_delete_item), color = Color.Red)
                                }
                            }
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)) {
                    if (isExpanded) {
                        hiddenText?.invoke()
                    }
                }
            }
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
            visible = isClicked.value
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.animateContentSize()
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
inline fun <reified T : Enum<T>> StatusMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    crossinline onSelect: (T) -> Unit,
    noinline onDismissRequest: () -> Unit,
    crossinline toColor: (T) -> Color,
    crossinline toResource: (T) -> Int
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        enumValues<T>().forEach { status ->
            DropdownMenuItem(onClick = { onSelect(status) }) {
                Text(
                    text = stringResource(toResource(status)),
                    color = toColor(status)
                )
            }
        }
    }
}