package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.database.entity.Game
import com.example.backlog.viewmodel.GameViewModel

@Composable
private fun StatusMenu(selected: MutableState<Int>, fieldModifier: Modifier, fieldShape: Shape) {
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

    Column() {
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
private fun ButtonCreate(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(stringResource(R.string.insert_button_add).uppercase())
    }
}

@Composable
fun GameCreationScreen(onDialogSubmitClick: () -> Unit, onEntryAddSuccess: () -> Unit,
                       gameViewModel: GameViewModel = viewModel()) {
    val values: SnapshotStateMap<Int, String> = remember { mutableStateMapOf() }
    val selectedStatus: MutableState<Int> = remember { mutableStateOf(R.string.status_not_started)}

    val isDialogOpened = remember { mutableStateOf(false) }
    val statusText = stringResource(selectedStatus.value)

    val fieldColumnModifier = Modifier.padding(vertical = 8.dp)
    val fieldModifier = Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    val fieldShape = AbsoluteRoundedCornerShape(4.dp)

    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)

    if (isDialogOpened.value) {
        val textModifier = Modifier.padding(horizontal = 16.dp)

        CancelDialog(onDismissRequest = { isDialogOpened.value = false }) {
            CancelDialogContent(
                onSubmitButtonClick = {
                    isDialogOpened.value = false
                    onDialogSubmitClick()
                },
                onStayButtonClick = { isDialogOpened.value = false },
                heading = R.string.insert_cancel_dialog_heading,
                description = R.string.insert_cancel_dialog_description,
                stayRes = R.string.insert_cancel_dialog_stay,
                returnRes = R.string.insert_cancel_dialog_return,
                modifier = textModifier
            )
        }
    }
    Scaffold(topBar = {
        SubScreenTopBar(heading = R.string.insert_topbar) { isDialogOpened.value = true }
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            FieldColumn(
                valueMap = values,
                fieldsRes = listOf(R.string.insert_game_title, R.string.insert_game_platform),
                modifier = fieldColumnModifier,
                fieldShape = fieldShape,
                fieldModifier = fieldModifier
            )
            StatusMenu(selectedStatus, fieldModifier, fieldShape)
            ButtonCreate {
                if (values.entries.all { it.value != "" }) {
                    val game = Game(
                        title = values[R.string.insert_game_title]!!.trim(),
                        platform = values[R.string.insert_game_platform]!!.trim(),
                        status = statusText,
                        coverPath = null
                    )
                    gameViewModel.insert(game)
                        .invokeOnCompletion {
                            if (it?.cause != null) {
                                successToast.show()
                                onEntryAddSuccess()
                            } else {
                                failureToast.show()
                            }
                        }
                }
            }
        }
    }
}
