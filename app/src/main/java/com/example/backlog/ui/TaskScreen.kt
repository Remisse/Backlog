package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.TaskStatus
import com.example.backlog.model.database.entity.TaskWithGameTitle
import com.example.backlog.viewmodel.TaskViewModel
import java.time.LocalDate
import java.util.*

@Composable
private fun TaskList(list: List<TaskWithGameTitle>, modifier: Modifier = Modifier, onEditClick: (Int) -> Unit,
                     onDeleteClick: (Int?) -> Unit, onChangeStatusClick: (Int) -> Unit) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(list) { item ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                exposedText = {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = stringResource(taskStatusToResource(item.task.status)).uppercase(),
                            style = MaterialTheme.typography.caption,
                            color = taskStatusToColor(item.task.status)
                        )
                        Text(text = item.task.description, style = MaterialTheme.typography.subtitle2)
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        CardSubtitleTextIcon(text = item.gameTitle, imageVector = Icons.Default.VideogameAsset)
                        item.task.deadlineDateEpochDay?.let { CardSubtitleTextIcon(
                            text = format.format(LocalDate.ofEpochDay(it)),
                            imageVector = Icons.Default.Event
                        ) }
                    }
                },
                hiddenText = null,
                onChangeStatusClick = { onChangeStatusClick(item.task.uid) } ,
                onEditClick = { onEditClick(item.task.uid) },
                onDeleteClick = { onDeleteClick(item.task.uid) }
            )
        }
    }
}

@Composable
fun TaskFab(modifier: Modifier = Modifier, onCreateClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text(stringResource(R.string.task_fab_add).uppercase()) },
        icon = { Icon(imageVector = Icons.Outlined.AddTask, contentDescription = null) },
        onClick = onCreateClick,
        modifier = modifier
    )
}

@Composable
fun TaskScreen(onTaskEditClick: (Int) -> Unit, taskViewModel: TaskViewModel = viewModel()) {
    val tasksFlow = taskViewModel.tasksWithGameTitle

    val taskUidChangeState: MutableState<Int?> = remember { mutableStateOf(null) }
    val resetChangeState = { taskUidChangeState.value = null }

    val taskUidToDelete: MutableState<Int?> = remember { mutableStateOf(null) }
    val resetDeleteState = { taskUidToDelete.value = null }
    val failureToast = Toast.makeText(LocalContext.current, stringResource(R.string.delete_failure_toast), Toast.LENGTH_SHORT)

    if (taskUidToDelete.value != null) {
        DeleteDialog(
            onDismissRequest = resetDeleteState,
            onConfirmDeleteClick = {
                taskViewModel.delete(
                    taskUidToDelete.value!!,
                    onSuccess = resetDeleteState,
                    onFailure = {
                        resetDeleteState()
                        failureToast.show()
                    }
                )
            },
            onCancelClick = resetDeleteState,
            body = R.string.task_delete_body
        )
    }
    if (taskUidChangeState.value != null) {
        Surface(shape = LookAndFeel.DialogSurfaceShape, modifier = Modifier.padding(16.dp)) {
            StatusMenu<TaskStatus>(
                expanded = taskUidChangeState.value != null,
                onSelect = {
                    taskViewModel.setStatus(taskUidChangeState.value!!, it)
                    resetChangeState()
                },
                onDismissRequest = resetChangeState,
                toColor = { taskStatusToColor(it) },
                toResource = { taskStatusToResource(it) }
            )
        }
    }
    tasksFlow.collectAsState(initial = listOf()).value.apply {
        if (this.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.tasks_empty),
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            TaskList(
                list = this,
                onChangeStatusClick = { taskUidChangeState.value = it },
                onEditClick = onTaskEditClick,
                onDeleteClick = { uid -> taskUidToDelete.value = uid }
            )
        }
    }
}
