package com.example.backlog.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.backlog.AppContainer
import com.example.backlog.R
import com.example.backlog.database.entities.Game
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.viewmodel.GameViewModel

@Composable
fun FormField(@StringRes res: Int, values: SnapshotStateMap<Int, String>, modifier: Modifier, shape: Shape) {
    values.putIfAbsent(res, "")

    OutlinedTextField(
        modifier = modifier,
        value = values[res].orEmpty(),
        onValueChange = { values[res] = it },
        label = { Text(stringResource(res))},
        shape = shape
    )
}

@Composable
fun InsertionForm(values: SnapshotStateMap<Int, String>, fieldModifier: Modifier, fieldShape: Shape) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        FormField(res = R.string.insert_game_title, values = values, fieldModifier, fieldShape)
        FormField(res = R.string.insert_game_platform, values = values, fieldModifier, fieldShape)
    }
}

@Composable
fun StatusMenu(selected: MutableState<Int>, fieldModifier: Modifier, fieldShape: Shape) {
    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
    val items = listOf(
        R.string.status_not_started,
        R.string.status_playing,
        R.string.status_completed,
        R.string.status_dropped
    )

    val source = remember { MutableInteractionSource() }
    if (source.collectIsPressedAsState().value) {
        isExpanded.value = !isExpanded.value
    }

    Column(
    ) {
        OutlinedTextField(
            label = { stringResource(R.string.insert_status_label) },
            value = stringResource(selected.value),
            onValueChange = { },
            modifier = fieldModifier,
            readOnly = true,
            shape = fieldShape,
            interactionSource = source
        )
        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
        ) {
            items.forEachIndexed() { index, res ->
                DropdownMenuItem(onClick = {
                    selected.value = items[index]
                    isExpanded.value = false
                }) {
                    Text(stringResource(res))
                }
            }
        }
    }
}

@Composable
fun ButtonCreate(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(stringResource(R.string.insert_button_add).uppercase())
    }
}

@Composable
fun CancelDialog(onReturnClick: () -> Unit, isDialogOpened: MutableState<Boolean>) {
    if (isDialogOpened.value) {
        Dialog(
            onDismissRequest = { isDialogOpened.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(shape = AbsoluteRoundedCornerShape(4.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)) {
                    Text(
                        text = stringResource(R.string.insert_back_dialog_heading),
                        style = MaterialTheme.typography.h6.plus(TextStyle(fontWeight = FontWeight.Bold)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Text(
                        stringResource(R.string.insert_cancel_dialog),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Button(
                            onClick = { isDialogOpened.value = false },
                            colors = ButtonDefaults.outlinedButtonColors()) {
                            Text(stringResource(R.string.insert_back_dialog_cancel).uppercase())
                        }
                        Button(onClick = onReturnClick) {
                            Text(stringResource(R.string.insert_back_dialog_submit).uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCreationScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    val values: SnapshotStateMap<Int, String> = remember { mutableStateMapOf() }
    val selectedStatus: MutableState<Int> = remember { mutableStateOf(R.string.status_not_started)}

    val isDialogOpened = remember { mutableStateOf(false) }
    val statusText = stringResource(selectedStatus.value)

    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val fieldShape = AbsoluteRoundedCornerShape(4.dp)

    BacklogTheme() {
        CancelDialog(onReturnClick = { navController.navigateUp() }, isDialogOpened)
        Scaffold(topBar = {
            TopAppBar() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { isDialogOpened.value = true }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                    Text(text = stringResource(R.string.insert_topbar), style = MaterialTheme.typography.h5)
                }
            }
        }) {
            Column {
                InsertionForm(values, fieldModifier, fieldShape)
                StatusMenu(selectedStatus, fieldModifier, fieldShape)
                ButtonCreate {
                    if (values.entries.all { it.value != "" }) {
                        val game = Game(
                            title = values[R.string.insert_game_title]!!,
                            platform = values[R.string.insert_game_platform]!!,
                            status = statusText,
                            coverPath = null
                        )
                        gameViewModel.insert(game)
                            .invokeOnCompletion { navController.navigateUp() }
                    }
                }
            }
        }
    }
}
