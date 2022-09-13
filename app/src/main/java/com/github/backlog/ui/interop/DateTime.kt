package com.github.backlog.ui.interop

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R
import com.github.backlog.ui.theme.Blue1
import com.github.backlog.ui.theme.YellowTetrad1

@Composable
private fun CustomDateTimeDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            content()
        }
    }
}

@Composable
fun Calendar(listener: DatePicker.OnDateChangedListener) {
    AndroidView(
        factory = { DatePicker(it) },
        update = { view -> view.setOnDateChangedListener(listener) }
    )
}

@Composable
fun CalendarDialog(
    enabled: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: (Int?, Int?, Int?) -> Unit
) {
    if (enabled) {
        val year: MutableState<Int?> = remember { mutableStateOf(null) }
        val month: MutableState<Int?> = remember { mutableStateOf(null) }
        val dayOfMonth: MutableState<Int?> = remember { mutableStateOf(null) }

        CustomDateTimeDialog(onDismissRequest = onDismissRequest) {
            // TODO Proper theming
            Surface(
                color = Color.White
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Calendar(listener = { _, y, m, d ->
                        year.value = y
                        month.value = m + 1
                        dayOfMonth.value = d
                    })
                    Row() {
                        Button(
                            enabled = year.value != null && month.value != null && dayOfMonth.value != null,
                            onClick = { onConfirmClick(year.value!!, month.value!!, dayOfMonth.value!!) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(stringResource(R.string.button_confirm).uppercase())
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Button(
                            onClick = { onConfirmClick(null, null, null) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(stringResource(R.string.card_delete_item).uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimePickerCompose(listener: TimePicker.OnTimeChangedListener) {
    AndroidView(
        factory = { TimePicker(it) },
        update =  { view -> view.setOnTimeChangedListener(listener) }
    )
}

@Composable
fun TimePickerDialogCompose(onDismissRequest: () -> Unit, onConfirmClick: (Int, Int) -> Unit) {
    val hour: MutableState<Int?> = remember { mutableStateOf(null) }
    val minute: MutableState<Int?> = remember { mutableStateOf(null) }

    CustomDateTimeDialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            TimePickerCompose(listener = { _, h, m ->
                hour.value = h
                minute.value = m
            })
            Button(
                onClick = { onConfirmClick(hour.value!!, minute.value!!) },
                enabled = hour.value != null && minute.value != null
            ) {
                Text(stringResource(R.string.button_confirm).uppercase())
            }
        }
    }
}
