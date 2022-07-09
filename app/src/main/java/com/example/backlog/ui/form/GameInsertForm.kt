package com.example.backlog.ui.form

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.database.GameStatus
import com.example.backlog.ui.*
import com.example.backlog.viewmodel.GameViewModel
import java.text.NumberFormat

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
        OutlinedTextField(
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

@Composable
private fun ButtonCreate(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(stringResource(R.string.insert_button_add).uppercase())
    }
}

// TODO Error dialog with all validators' error messages when clicking the Create button
@Composable
fun GameInsertScreen(onDialogSubmitClick: () -> Unit, onEntryAddSuccess: () -> Unit,
                     gameViewModel: GameViewModel = viewModel()) {
    val state = remember { gameViewModel.formState }

    val maxPrice = 200.0f

    val successToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)

    if (state.showCancelDialog) {
        CancelDialog(onDismissRequest = { state.showCancelDialog = false }) {
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
    }
    Scaffold(topBar = {
        SubScreenTopBar(heading = R.string.insert_topbar) { state.showCancelDialog = true }
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = state.title.value,
                onValueChange = { state.title.value = it },
                isError = state.title.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_title)) },
                modifier = LookAndFeel.FieldModifier
            )
            OutlinedTextField(
                value = state.platform.value,
                onValueChange = { state.platform.value = it },
                isError = state.platform.hasErrors(),
                label = { Text(stringResource(R.string.insert_game_platform)) },
                modifier = LookAndFeel.FieldModifier,
                shape = LookAndFeel.FieldShape
            )
            Text(
                text = stringResource(R.string.game_insert_retail_price),
                modifier = LookAndFeel.TextFieldTitleModifier,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = NumberFormat.getCurrencyInstance().format(state.retailPrice.value),
                modifier = LookAndFeel.TextValueModifier,
                style = MaterialTheme.typography.subtitle2
            )
            Slider(
                value = state.retailPrice.value,
                onValueChange = { state.retailPrice.value = it },
                modifier = LookAndFeel.FieldModifier,
                valueRange = 0f.rangeTo(maxPrice)
            )
            StatusMenu(
                value = state.status.value,
                onSelect = {
                    state.status.value = it
                })
            ButtonCreate {
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
        }
    }
}
