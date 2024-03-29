package com.github.backlog.ui.screen.secondary.taskform

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.*
import com.github.backlog.ui.interop.CalendarDialog
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.ui.state.form.TaskFormState
import java.time.LocalDate
import java.util.*

@Composable
private fun GameSelectionDialogWithSearch(
    onDismissRequest: () -> Unit,
    onGameSelect: (Game) -> Unit,
    gameViewModel: GameViewModel,
    enabled: Boolean
) {
    if (!enabled) return

    val searchText = remember { mutableStateOf("") }
    val games = gameViewModel.backlog
        .collectAsState(initial = listOf())
        .value

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
                    onSearchClick = { },
                    modifier = Modifier.fillMaxWidth()
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
                                val platform = if (game.platform != null) " (${game.platform})" else ""

                                Text(text = "${game.title}$platform")
                                Spacer(modifier = Modifier.weight(0.001f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormContent(
    onSubmitClick: (TaskFormState) -> Unit,
    onCancelDialogSubmit: () -> Unit,
    state: TaskFormState,
    @StringRes topBarTitle: Int,
    @StringRes submitButton: Int,
    gameViewModel: GameViewModel
) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    val gameSelectionInteractionSource = remember { MutableInteractionSource() }
    if (gameSelectionInteractionSource.collectIsPressedAsState().value) {
        state.showGameSelection = true
    }

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        state.showDatePicker = true
    }

    CancelDialog(
        enabled = state.showCancelDialog,
        onDismissRequest = { state.showCancelDialog = false }
    ) {
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
    CalendarDialog(
        enabled = state.showDatePicker,
        onDismissRequest = { state.showDatePicker = false },
        onConfirmClick = { year, month, dayOfMonth ->
            state.deadline.value =
                if (year != null && month != null && dayOfMonth != null)
                    LocalDate.of(year, month, dayOfMonth)
                else null
            state.showDatePicker = false
        }
    )
    GameSelectionDialogWithSearch(
        enabled = state.showGameSelection,
        onDismissRequest = { state.showGameSelection = false},
        onGameSelect = {
            val platform = if (it.platform != null) " (${it.platform})" else ""

            state.gameAndPlatform = "${it.title}${platform}"
            state.gameId.value = it.uid
            state.showGameSelection = false
        },
        gameViewModel = gameViewModel
    )
    Column(
        modifier = LookAndFeel.FieldColumnModifier,
        verticalArrangement = LookAndFeel.FieldColumnVerticalArrangement
    ) {
        BacklogTextField(
            value = state.gameAndPlatform,
            onValueChange = {},
            label = { Text(stringResource(R.string.task_field_game)) },
            interactionSource = gameSelectionInteractionSource,
            readOnly = true
        )
        BacklogTextField(
            value = state.description.value,
            onValueChange = { state.description.value = it },
            label = { Text(stringResource(R.string.task_field_description)) },
            isError = state.description.hasErrors()
        )
        BacklogTextField(
            value = if (state.deadline.value != null) format.format(state.deadline.value) else "",
            onValueChange = {},
            label = { Text(stringResource(R.string.task_field_deadline_date)) },
            readOnly = true,
            interactionSource = dateInteractionSource
        )
        Button(
            modifier = LookAndFeel.FieldModifier,
            onClick = { onSubmitClick(state) }
        ) {
            Text(text = stringResource(submitButton), style = MaterialTheme.typography.labelLarge)
        }
    }
}
