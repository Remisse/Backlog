package com.github.backlog.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.ui.state.form.FormElement

@Composable
fun TextLabel(
    text: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        label()
        text()
    }
}

// onChangeStatusClick, onEditClick and onDeleteClick will generate errors at compile time if inlined
@Composable
inline fun ItemCard(
    modifier: Modifier,
    crossinline exposedText: @Composable () -> Unit,
    noinline hiddenText: (@Composable () -> Unit)?,
    noinline onChangeStatusClick: () -> Unit,
    noinline onEditClick: () -> Unit,
    noinline onDeleteClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showActionMenu by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isExpanded, label = "ExpandClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 180f }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    ) {
        Surface(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
            Column(modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1.0f, true)) {
                        exposedText()
                    }
                    Column(modifier = Modifier.weight(0.35f, true)) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { showActionMenu = !showActionMenu }) {
                                Icon(
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = stringResource(R.string.alt_card_menu_button)
                                )
                            }
                            if (hiddenText != null) {
                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = stringResource(R.string.alt_card_details_button),
                                        modifier = Modifier.rotate(rotation.value)
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showActionMenu,
                                onDismissRequest = { showActionMenu = false },
                                modifier = Modifier.padding(vertical = 0.dp)
                            ) {
                                DropdownMenuItem(
                                    interactionSource = remember { MutableInteractionSource() },
                                    leadingIcon = { Icon(imageVector = Icons.Outlined.Check, contentDescription = null) },
                                    text = { Text(stringResource(R.string.change_status_button)) },
                                    onClick = {
                                        showActionMenu = false
                                        onChangeStatusClick()
                                    }
                                )
                                DropdownMenuItem(
                                    interactionSource = remember { MutableInteractionSource() },
                                    leadingIcon = { Icon(imageVector = Icons.Outlined.Edit, contentDescription = null) },
                                    text = { Text(stringResource(R.string.edit_button)) },
                                    onClick = {
                                        showActionMenu = false
                                        onEditClick()
                                    }
                                )
                                Divider()
                                DropdownMenuItem(
                                    interactionSource = remember { MutableInteractionSource() },
                                    leadingIcon = { Icon(imageVector = Icons.Outlined.Delete, contentDescription = null) },
                                    text = { Text(text = stringResource(R.string.card_delete_item), color = Color.Red) },
                                    onClick = {
                                        showActionMenu = false
                                        onDeleteClick()
                                    }
                                )
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
fun ActionsFab(
    icon: ImageVector,
    modifier: Modifier,
    miniFabs: @Composable () -> Unit
) {
    val isClicked: MutableState<Boolean> = remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isClicked.value, label = "FabClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else -45f }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End,
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
        FloatingActionButton(
            onClick = { isClicked.value = !isClicked.value },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.rotate(rotation.value)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormTextField(
    element: FormElement<T>,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = LookAndFeel.FieldShape
) {
    TextField(
        value = element.value.toString(),
        onValueChange = onValueChange,
        isError = element.hasErrors(),
        label = label,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = TextFieldDefaults.textFieldColors(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BacklogTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    label: @Composable () -> Unit,
    shape: Shape = LookAndFeel.FieldShape,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    readOnly: Boolean = false,
    singleLine: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        label = label,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = TextFieldDefaults.textFieldColors(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        interactionSource = interactionSource,
        readOnly = readOnly,
        singleLine = singleLine
    )
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
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(toResource(status)),
                        color = toColor(status)
                    )
                },
                onClick = { onSelect(status) }
            )
        }
    }
}