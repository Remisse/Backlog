package com.example.backlog.ui.form

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.database.entity.Game
import com.example.backlog.ui.*
import com.example.backlog.ui.interop.CalendarDialog
import com.example.backlog.viewmodel.GameViewModel
import com.example.backlog.viewmodel.TaskViewModel
import com.example.backlog.ui.state.TaskFormState
import java.time.LocalDate

@Composable
private fun GameSelectionDialogWithSearch(onDismissRequest: () -> Unit, onGameSelect: (Game) -> Unit,
                                          gameViewModel: GameViewModel) {
    val searchText = remember { mutableStateOf("") }
    val gamesFlow = gameViewModel.backlog
    val games = gamesFlow.collectAsState(initial = listOf()).value

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
                    items(games.filter {
                        searchText.value == "" || searchText.value.lowercase() in it.title.lowercase()
                    }) { game ->
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
fun TaskFormContent(onSubmitClick: (TaskFormState) -> Unit, onCancelDialogSubmit: () -> Unit,
                    state: TaskFormState, @StringRes topBarTitle: Int, @StringRes submitButton: Int,
                    gameViewModel: GameViewModel) {
    val gameSelectionInteractionSource = remember { MutableInteractionSource() }
    if (gameSelectionInteractionSource.collectIsPressedAsState().value) {
        state.showGameSelection = true
    }

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        state.showDatePicker = true
    }

    if (state.showCancelDialog) {
        CancelDialog(onDismissRequest = { state.showCancelDialog = false }) {
            CancelDialogContent(
                heading = R.string.task_insert_cancel_heading,
                description = R.string.task_insert_cancel_description,
                stayRes = R.string.insert_cancel_dialog_stay,
                returnRes = R.string.task_insert_cancel_return,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onStayButtonClick = { state.showCancelDialog = false },
                onSubmitButtonClick = {
                    state.showCancelDialog = false
                    onCancelDialogSubmit()
                }
            )
        }
    }
    if (state.showDatePicker) {
        CalendarDialog(
            onDismissRequest = { state.showDatePicker = false },
            onConfirmClick = { year, month, dayOfMonth ->
                state.deadline.value = LocalDate.of(year, month, dayOfMonth)
                state.showDatePicker = false
            })
    }
    if (state.showGameSelection) {
        GameSelectionDialogWithSearch(
            onDismissRequest = { state.showGameSelection = false},
            onGameSelect = {
                state.gameAndPlatform = "${it.title} (${it.platform})"
                state.gameId.value = it.uid
                state.showGameSelection = false
            },
            gameViewModel = gameViewModel
        )
    }
    Scaffold(
        topBar = {
            SubScreenTopBar(heading = topBarTitle) { state.showCancelDialog = true }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = state.gameAndPlatform,
                onValueChange = {},
                label = { Text(stringResource(R.string.task_field_game)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape,
                interactionSource = gameSelectionInteractionSource,
                readOnly = true
            )
            OutlinedTextField(
                value = state.description.value,
                onValueChange = { state.description.value = it },
                label = { Text(stringResource(R.string.task_field_description)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape,
                isError = state.description.hasErrors()
            )
            OutlinedTextField(
                value = state.deadline.value.toString(),
                onValueChange = {},
                label = { Text(stringResource(R.string.task_field_deadline_date)) },
                readOnly = true,
                modifier = LookAndFeel.FieldModifier,
                interactionSource = dateInteractionSource,
                shape = LookAndFeel.FieldShape
            )
            Button(
                modifier = LookAndFeel.FieldModifier,
                onClick = { onSubmitClick(state) }
            ) {
                Text(stringResource(submitButton).uppercase())
            }
        }
    }
}

@Composable
fun TaskInsertScreen(onEntryAddSuccess: () -> Unit, onCancelDialogSubmit: () -> Unit,
                     gameViewModel: GameViewModel = viewModel(),
                     taskViewModel: TaskViewModel = viewModel()) {
    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.task_insert_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.task_insert_failure_toast), Toast.LENGTH_SHORT)

    TaskFormContent(
        onSubmitClick = {
            if (it.validateAll()) {
                taskViewModel.insert(
                    task = it.toEntity(),
                    onSuccess = {
                        successToast.show()
                        onEntryAddSuccess()
                    },
                    onFailure = {
                        failureToast.show()
                    }
                )
            }
        },
        onCancelDialogSubmit = onCancelDialogSubmit,
        state = taskViewModel.formState,
        topBarTitle = R.string.task_fab_add,
        submitButton = R.string.task_fab_add,
        gameViewModel = gameViewModel
    )
}

