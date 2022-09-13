package com.github.backlog.ui.screen.main.tasks

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import com.github.backlog.ui.components.*
import com.github.backlog.utils.localDateFromEpochSecond
import com.github.backlog.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*

@Composable
private fun TaskList(
    list: List<TaskWithGameTitle>,
    modifier: Modifier = Modifier,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int?) -> Unit,
    onChangeStatusClick: (Int) -> Unit
) {
    val format = LookAndFeel.dateFormat(Locale.getDefault())

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(list) { item ->
            Log.d("tasks", "Task ${item.task.uid} notified: ${item.task.notified}")

            ItemCard(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                exposedText = {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(item.task.status.toResource()).uppercase(),
                            style = MaterialTheme.typography.caption,
                            color = item.task.status.toColor()
                        )
                        Text(text = item.task.description, style = MaterialTheme.typography.subtitle2)
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        CardSubtitleTextLabel(
                            text = item.gameTitle,
                            label = { Text(
                                text = stringResource(R.string.task_field_game),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary
                            ) },
                        )
                        item.task.deadline?.let { CardSubtitleTextLabel(
                            text = format.format(localDateFromEpochSecond(it)),
                            label = { Text(
                                text = stringResource(R.string.task_card_icon_deadline),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary
                            ) }
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
    FloatingActionButton(
        onClick = onCreateClick,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
    }
}

// TODO Don't pass the ViewModel directly
@Composable
fun TaskScreenContent(
    onTaskEditClick: (Int) -> Unit,
    taskViewModel: TaskViewModel
) {
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
                toColor = { it.toColor() },
                toResource = { it.toResource() }
            )
        }
    }
    tasksFlow.collectAsState(initial = listOf()).value
        .apply {
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
