package com.github.backlog.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R

@Composable
fun CancelDialog(
    enabled: Boolean,
    onDismissRequest: () -> Unit,
    dialogContent: @Composable () -> Unit
) {
    if (enabled) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                modifier = Modifier.padding(4.dp),
                shape = LookAndFeel.DialogSurfaceShape
            ) {
                dialogContent()
            }
        }
    }
}

@Composable
fun CancelDialogContent(
    @StringRes heading: Int,
    @StringRes description: Int,
    @StringRes stayRes: Int,
    @StringRes returnRes: Int,
    modifier: Modifier,
    onStayButtonClick: () -> Unit,
    onSubmitButtonClick: () -> Unit
) {
    Column(
        verticalArrangement = LookAndFeel.DialogVerticalArrangement,
        horizontalAlignment = LookAndFeel.DialogHorizontalAlignment,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(heading),
            style = MaterialTheme.typography.headlineSmall.plus(TextStyle(fontWeight = FontWeight.Bold)),
            modifier = modifier
        )
        Text(
            text = stringResource(description),
            textAlign = TextAlign.Start,
            modifier = modifier
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = modifier
        ) {
            TextButton(onClick = onStayButtonClick) {
                Text(stringResource(stayRes))
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Button(onClick = onSubmitButtonClick) {
                Text(stringResource(returnRes))
            }
        }
    }
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    @StringRes body: Int
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(shape = LookAndFeel.DialogSurfaceShape) {
            Column(
                verticalArrangement = LookAndFeel.DialogVerticalArrangement,
                horizontalAlignment = LookAndFeel.DialogHorizontalAlignment,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.dialog_warning_heading),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(body),
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text(stringResource(R.string.insert_button_cancel))
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Button(onClick = onConfirmDeleteClick) {
                        Text(stringResource(R.string.card_delete_item))
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorDialog(errorMessage: String, onConfirmClick: () -> Unit, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface() {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(text = errorMessage)
                Button(onClick = onConfirmClick) {
                    Text(text = "OK")
                }
            }
        }
    }
}
