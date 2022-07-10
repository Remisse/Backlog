package com.example.backlog.ui.form

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.GameStatus
import com.example.backlog.ui.*
import com.example.backlog.ui.interop.CalendarDialog
import com.example.backlog.viewmodel.GameViewModel
import java.time.LocalDate
import java.util.*

@Composable
private fun StatusMenu(value: GameStatus, onSelect: (GameStatus) -> Unit) {
    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }

    val source = remember { MutableInteractionSource() }
    if (source.collectIsPressedAsState().value) {
        isExpanded.value = !isExpanded.value
    }

    Column(
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            label = { Text(text = stringResource(R.string.insert_status_label)) },
            value = stringResource(gameStatusToResource(value)),
            onValueChange = { },
            trailingIcon = {
                Icon(
                    imageVector = if (isExpanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = LookAndFeel.FieldModifier,
            readOnly = true,
            shape = LookAndFeel.FieldShape,
            interactionSource = source,
            colors = TextFieldDefaults.textFieldColors(textColor = gameStatusToColor(value))
        )
        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = LookAndFeel.FieldModifier
        ) {
            GameStatus.values().forEach { status ->
                DropdownMenuItem(onClick = {
                    isExpanded.value = false
                    onSelect(status)
                }) {
                    Text(
                        text = stringResource(gameStatusToResource(status)),
                        color = gameStatusToColor(status)
                    )
                }
            }
        }
    }
}

// TODO Error dialog with all validators' error messages when clicking the Create button
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameInsertScreen(onDialogSubmitClick: () -> Unit, onEntryAddSuccess: () -> Unit,
                     gameViewModel: GameViewModel = viewModel()) {
    val state = remember { gameViewModel.formState }

    val format = LookAndFeel.dateFormat(Locale.getDefault())

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        state.showCalendar = true
    }

    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)

    CancelDialog(
        enabled = state.showCancelDialog,
        onDismissRequest = { state.showCancelDialog = false }
    ) {
        CancelDialogContent(
            onSubmitButtonClick = {
                state.showCancelDialog = false
                onDialogSubmitClick()
            },
            onStayButtonClick = { state.showCancelDialog = false },
            heading = R.string.insert_cancel_dialog_heading,
            description = R.string.insert_cancel_dialog_description,
            stayRes = R.string.insert_cancel_dialog_stay,
            returnRes = R.string.insert_cancel_dialog_return,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
    CalendarDialog(
        enabled = state.showCalendar,
        onDismissRequest = { state.showCalendar = false },
        onConfirmClick = { year, month, dayOfMonth ->
            state.releaseDate.value = LocalDate.of(year, month, dayOfMonth)
            state.showCalendar = false
        }
    )
    Scaffold(topBar = {
        SubScreenTopBar(heading = R.string.insert_topbar) { state.showCancelDialog = true }
    }) {
        Column(
            modifier = LookAndFeel.FieldColumnModifier,
            verticalArrangement = LookAndFeel.FieldColumnVerticalArrangement
        ) {
            TextField(
                value = state.title.value,
                onValueChange = { state.title.value = it },
                isError = state.title.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_title)) },
                modifier = LookAndFeel.FieldModifier
            )
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            TextField(
                value = state.platform.value,
                onValueChange = { state.platform.value = it },
                isError = state.platform.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_platform)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            TextField(
                value = state.genre.value,
                onValueChange = { state.genre.value = it },
                isError = state.genre.hasErrors(),
                label = { Text(stringResource(R.string.game_insert_genre)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            TextField(
                value = if (state.releaseDate.value != null) format.format(state.releaseDate.value) else "",
                onValueChange = {},
                label = { Text(stringResource(R.string.game_insert_release_date)) },
                readOnly = true,
                modifier = LookAndFeel.FieldModifier,
                interactionSource = dateInteractionSource,
                shape = LookAndFeel.FieldShape
            )
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            TextField(
                value = state.developer.value,
                onValueChange = { state.developer.value = it },
                isError = state.developer.hasErrors(),
                label = { Text(stringResource(R.string.game_insert_developer)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            TextField(
                value = state.publisher.value,
                onValueChange = { state.publisher.value = it },
                isError = state.publisher.hasErrors(),
                label = { Text(stringResource(R.string.game_insert_publisher)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            StatusMenu(
                value = state.status.value,
                onSelect = {
                    state.status.value = it
                })
            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                onClick = {
                    if (state.validateAll()) {
                        gameViewModel.insert(
                            game = state.toEntity(),
                            onSuccess = {
                                successToast.show()
                                onEntryAddSuccess()
                            },
                            onFailure = {
                                failureToast.show()
                            }
                        )
                    }
                }
            ) {
                Text(stringResource(R.string.insert_button_add).uppercase())
            }
        }
    }
}
