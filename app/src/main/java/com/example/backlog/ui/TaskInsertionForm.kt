package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.backlog.R
import com.example.backlog.database.entity.Game
import com.example.backlog.database.entity.Task
import com.example.backlog.ui.interop.DatePickerDialogCompose
import com.example.backlog.ui.interop.TimePickerDialogCompose
import com.example.backlog.viewmodel.GameViewModel
import com.example.backlog.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
private fun GameSelectionDialogWithSearch(onDismissRequest: () -> Unit, onGameSelect: (Game) -> Unit,
                                          gameViewModel: GameViewModel) {
    val searchText = remember { mutableStateOf("") }
    val games = gameViewModel.backlog.observeAsState()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 32.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SearchBar(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.animateContentSize()
                ) {
                    items(games.value?.filter { searchText.value == "" || searchText.value.lowercase() in it.title.lowercase()}
                        .orEmpty()
                    ) { game ->
                        OutlinedButton(
                            onClick = { onGameSelect(game) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors()
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "${game.title} (${game.platform})")
                                Spacer(modifier = Modifier.weight(0.001f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCreationScreen(onEntryAddSuccess: () -> Unit, onDialogSubmitClick: () -> Unit,
                       gameViewModel: GameViewModel, taskViewModel: TaskViewModel) {
    val taskDescription: MutableState<String?> = remember { mutableStateOf(null) }
    val taskGame: MutableState<Int?> = remember { mutableStateOf(null) }
    val taskDateEpochDay: MutableState<Long?> = remember { mutableStateOf(null) }
    val taskTimeSecondsOfDay: MutableState<Int?> = remember { mutableStateOf(null) }

    val status = stringResource(R.string.task_status_in_progress)

    val gameSelectionFieldValue = remember { mutableStateOf("") }

    val gameSelectionInteractionSource = remember { MutableInteractionSource() }
    val showGameSelection = remember { mutableStateOf(false) }
    if (gameSelectionInteractionSource.collectIsPressedAsState().value) {
        showGameSelection.value = true
    }

    val dateInteractionSource = remember { MutableInteractionSource() }
    val showDatePicker = remember { mutableStateOf(false) }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        showDatePicker.value = true
    }

    val timeInteractionSource = remember { MutableInteractionSource() }
    val showTimePicker = remember { mutableStateOf(false) }
    if (timeInteractionSource.collectIsPressedAsState().value) {
        showTimePicker.value = true
    }

    val showCancelDialog = remember { mutableStateOf(false) }

    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val fieldShape = RoundedCornerShape(4.dp)

    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.task_insert_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.task_insert_failure_toast), Toast.LENGTH_SHORT)

    if (showCancelDialog.value) {
        CancelDialog(onDismissRequest = { showCancelDialog.value = false }) {
            CancelDialogContent(
                heading = R.string.task_insert_cancel_heading,
                description = R.string.task_insert_cancel_description,
                stayRes = R.string.insert_cancel_dialog_stay,
                returnRes = R.string.task_insert_cancel_return,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onStayButtonClick = { showCancelDialog.value = false },
                onSubmitButtonClick = {
                    showCancelDialog.value = false
                    onDialogSubmitClick()
                }
            )
        }
    }
    if (showTimePicker.value) {
        TimePickerDialogCompose(
            onDismissRequest = { showTimePicker.value = false },
            onConfirmClick = { hour, minute ->
                taskTimeSecondsOfDay.value = LocalTime.of(hour, minute).toSecondOfDay()
                showTimePicker.value = false
            }
        )
    }
    if (showDatePicker.value) {
        DatePickerDialogCompose(
            onDismissRequest = { showDatePicker.value = false },
            onConfirmClick = { year, month, dayOfMonth ->
                taskDateEpochDay.value = LocalDate.of(year, month, dayOfMonth).toEpochDay()
                showDatePicker.value = false
            })
    }
    if (showGameSelection.value) {
        GameSelectionDialogWithSearch(
            onDismissRequest = { showGameSelection.value = false},
            onGameSelect = {
                gameSelectionFieldValue.value = "${it.title} (${it.platform})"
                taskGame.value = it.uid
                showGameSelection.value = false
            },
            gameViewModel = gameViewModel
        )
    }
    Scaffold(
        topBar = {
            SubScreenTopBar(heading = R.string.task_fab_add) { showCancelDialog.value = true }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = gameSelectionFieldValue.value,
                onValueChange = {},
                label = { Text(stringResource(R.string.task_field_game)) },
                modifier = fieldModifier,
                shape = fieldShape,
                interactionSource = gameSelectionInteractionSource,
                readOnly = true
            )
            OutlinedTextField(
                value = taskDescription.value.orEmpty(),
                onValueChange = { taskDescription.value = it },
                label = { Text(stringResource(R.string.task_field_description)) },
                modifier = fieldModifier,
                shape = fieldShape
            )

            val dateFieldValue = when (taskDateEpochDay.value) {
                null -> ""
                else -> LocalDate.ofEpochDay(taskDateEpochDay.value!!).toString()
            }
            OutlinedTextField(
                value = dateFieldValue,
                onValueChange = {},
                label = { Text(stringResource(R.string.task_field_deadline_date)) },
                readOnly = true,
                modifier = fieldModifier,
                interactionSource = dateInteractionSource,
                shape = fieldShape
            )

            val timeFieldValue = when (taskTimeSecondsOfDay.value) {
                null -> ""
                else -> LocalTime.ofSecondOfDay(taskTimeSecondsOfDay.value!!.toLong()).toString()
            }
            OutlinedTextField(
                value = timeFieldValue,
                onValueChange = {},
                label = { Text(stringResource(R.string.task_field_deadline_time)) },
                readOnly = true,
                modifier = fieldModifier,
                interactionSource = timeInteractionSource,
                shape = fieldShape
            )
            Button(
                modifier = fieldModifier,
                onClick = {
                    if (taskDescription.value != null && taskGame.value != null) {
                        val task = Task(
                            description = taskDescription.value!!.trim(),
                            gameId = taskGame.value!!,
                            deadlineDateEpochDay = taskDateEpochDay.value,
                            deadlineTimeSeconds = taskTimeSecondsOfDay.value,
                            status = status
                        )

                        taskViewModel.insert(task)
                            .invokeOnCompletion {
                                if (it?.cause == null) {
                                    successToast.show()
                                    onEntryAddSuccess()
                                } else {
                                    failureToast.show()
                                }

                            }
                    }
            }) {
                Text(stringResource(R.string.task_fab_add).uppercase())
            }
        }
    }
}
