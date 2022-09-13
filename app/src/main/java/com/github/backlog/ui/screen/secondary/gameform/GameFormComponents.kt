package com.github.backlog.ui.screen.secondary.gameform

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.ui.components.*
import com.github.backlog.ui.interop.CalendarDialog
import com.github.backlog.ui.state.form.FormElement
import com.github.backlog.ui.state.form.GameFormState
import java.time.LocalDate
import java.util.*

@Composable
private fun FieldStatusMenu(
    value: GameStatus,
    onSelect: (GameStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isExpanded, label = "ExpandClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 180f }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        TextField(
            label = { Text(text = stringResource(R.string.insert_status_label)) },
            value = stringResource(value.toResource()),
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
            toColor = { it.toColor() },
            toResource = { it.toResource() }
        )
    }
}

@Composable
private fun <T> FormTextField(
    element: FormElement<T>,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = LookAndFeel.FieldModifier,
    shape: Shape = LookAndFeel.FieldShape
) {
    TextField(
        value = element.value.toString(),
        onValueChange = onValueChange,
        isError = element.hasErrors(),
        label = label,
        modifier = modifier,
        shape = shape
    )
}

@Composable
fun GameFormContent(
    state: GameFormState,
    onCancelDialogConfirm: () -> Unit,
    onCommitButtonClick: () -> Unit,
    @StringRes button: Int
) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        state.showDatePicker = true
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
        enabled = state.showDatePicker,
        onDismissRequest = { state.showDatePicker = false }
    ) { year, month, dayOfMonth ->
        state.completionDate.value =
            if (year != null && month != null && dayOfMonth != null)
                LocalDate.of(year, month, dayOfMonth)
            else null
        state.showDatePicker = false
    }
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
        FormTextField(
            element = state.title,
            onValueChange = { state.title.value = it },
            label = { Text(stringResource(R.string.insert_game_title)) },
        )
        Text(
            text = stringResource(R.string.optional_fields_heading),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1,
            modifier = LookAndFeel.TextFieldTitleModifier
        )
        FormTextField(
            element = state.genre,
            onValueChange = { state.genre.value = it },
            label = { Text(stringResource(R.string.game_insert_genre)) }
        )
        FormTextField(
            element = state.platform,
            onValueChange = { state.platform.value = it },
            label = { Text(stringResource(R.string.insert_game_platform)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            FieldStatusMenu(
                value = state.status.value,
                onSelect = { state.status.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5f)
            )
            TextField(
                value = if (state.completionDate.value != null) format.format(state.completionDate.value) else "",
                onValueChange = {},
                label = { Text(stringResource(R.string.game_form_completion_date_label)) },
                readOnly = true,
                modifier = LookAndFeel.FieldModifier.weight(.5f),
                interactionSource = dateInteractionSource,
                shape = LookAndFeel.FieldShape
            )
        }
        FormTextField(
            element = state.developer,
            onValueChange = { state.developer.value = it },
            label = { Text(stringResource(R.string.game_insert_developer)) }
        )
        FormTextField(
            element = state.publisher,
            onValueChange = { state.publisher.value = it },
            label = { Text(stringResource(R.string.game_insert_publisher)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            FormTextField(
                element = state.steamId,
                onValueChange = { state.steamId.value = it },
                label = { Text(stringResource(R.string.gameform_steamid_label)) },
                modifier = Modifier.weight(.5f)
            )
            FormTextField(
                element = state.rawgId,
                onValueChange = { state.rawgId.value = it },
                label = { Text(stringResource(R.string.gameform_rawgid_label)) },
                modifier = Modifier.weight(.5f)
            )
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = onCommitButtonClick) {
            Text(stringResource(button).uppercase())
        }
    }
}

@Preview
@Composable
fun GameFormContentPreview() {
    Surface() {
        GameFormContent(
            state = GameFormState(),
            onCancelDialogConfirm = { },
            onCommitButtonClick = { },
            button = R.string.edit_button
        )
    }
}
