package com.example.backlog.ui.form

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.GameStatus
import com.example.backlog.ui.*
import com.example.backlog.ui.interop.CalendarDialog
import com.example.backlog.ui.state.GameFormState
import com.example.backlog.viewmodel.GameViewModel
import java.time.LocalDate
import java.util.*

@Composable
private fun FieldStatusMenu(value: GameStatus, onSelect: (GameStatus) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isExpanded, label = "ExpandClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 180f }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        TextField(
            label = { Text(text = stringResource(R.string.insert_status_label)) },
            value = stringResource(gameStatusToResource(value)),
            onValueChange = { },
            trailingIcon = {
                IconButton(onClick = { isExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation.value)
                    )
                }
            },
            modifier = LookAndFeel.FieldModifier,
            readOnly = true,
            shape = LookAndFeel.FieldShape
        )
        StatusMenu<GameStatus>(
            expanded = isExpanded,
            onSelect = {
                isExpanded = false
                onSelect(it)
            },
            onDismissRequest = { isExpanded = false },
            toColor = { gameStatusToColor(it) },
            toResource = { gameStatusToResource(it) }
        )
    }
}

// TODO Error dialog with all validators' error messages when clicking the Create button
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameFormContent(state: GameFormState, onCancelDialogConfirm: () -> Unit,
                    onCommitButtonClick: (GameFormState) -> Unit, @StringRes button: Int) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        state.showCalendar = true
    }

    CancelDialog(
        enabled = state.showCancelDialog,
        onDismissRequest = { state.showCancelDialog = false }
    ) {
        CancelDialogContent(
            onSubmitButtonClick = {
                state.showCancelDialog = false
                onCancelDialogConfirm()
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
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = LookAndFeel.FieldColumnVerticalArrangement
        ) {
            Text(
                text = stringResource(R.string.required_fields_heading),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1
            )
            TextField(
                value = state.title.value,
                onValueChange = { state.title.value = it },
                isError = state.title.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_title)) },
                modifier = LookAndFeel.FieldModifier
            )
            TextField(
                value = state.platform.value,
                onValueChange = { state.platform.value = it },
                isError = state.platform.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_platform)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(
                text = stringResource(R.string.optional_fields_heading),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                modifier = LookAndFeel.TextFieldTitleModifier
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
            FieldStatusMenu(
                value = state.status.value,
                onSelect = {
                    state.status.value = it
                })
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onCommitButtonClick(state) }) {
                Text(stringResource(button).uppercase())
            }
        }
    }
}

@Composable
fun GameInsertScreen(onDialogSubmitClick: () -> Unit, onEntryAddSuccess: () -> Unit,
                     gameViewModel: GameViewModel = viewModel()) {
    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_failure_toast), Toast.LENGTH_SHORT)

    GameFormContent(
        state = remember { GameFormState() },
        button = R.string.insert_button_add,
        onCancelDialogConfirm = onDialogSubmitClick,
        onCommitButtonClick = { state ->
            if (state.validateAll()) {
                gameViewModel.insert(
                    entity = state.toEntity(),
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
    )
}
