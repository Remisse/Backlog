package com.example.backlog.ui.interop

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.backlog.R

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
fun DatePickerCompose(listener: DatePicker.OnDateChangedListener) {
    AndroidView(
        factory = { DatePicker(it) },
        update = { view -> view.setOnDateChangedListener(listener) }
    )
}

@Composable
fun DatePickerDialogCompose(onDismissRequest: () -> Unit, onConfirmClick: (Int, Int, Int) -> Unit) {
    val year: MutableState<Int?> = remember { mutableStateOf(null) }
    val month: MutableState<Int?> = remember { mutableStateOf(null) }
    val dayOfMonth: MutableState<Int?> = remember { mutableStateOf(null) }

    CustomDateTimeDialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            DatePickerCompose(listener = { _, y, m, d ->
                year.value = y
                month.value = m + 1 // Java...
                dayOfMonth.value = d
            })
            Button(
                onClick = { onConfirmClick(year.value!!, month.value!!, dayOfMonth.value!!) },
                enabled = year.value != null && month.value != null && dayOfMonth.value != null
            ) {
                Text(stringResource(R.string.button_confirm).uppercase())
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
