package com.example.backlog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.model.database.TaskStatus
import com.example.backlog.model.database.entity.TaskWithGameTitle
import com.example.backlog.viewmodel.TaskViewModel
import java.time.LocalDate

@Composable
private fun TaskList(list: List<TaskWithGameTitle>, modifier: Modifier, onEditClick: (Int) -> Unit,
                     onDeleteClick: (Int?) -> Unit) {
    val iconModifier = Modifier.size(10.dp)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(list) { item ->
            ItemCard(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                topText = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(taskStatusToResource(item.task.status)).uppercase(),
                            fontSize = 10.sp,
                            color = taskStatusToColor(item.task.status)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                        Text(text = item.task.description, fontWeight = FontWeight.SemiBold)
                    }
                },
                subText = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideogameAsset,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                        Text(
                            text = item.gameTitle,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                        Text(
                            text = LocalDate.ofEpochDay(item.task.deadlineDateEpochDay as Long).toString(),
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    }
                },
                hiddenText = null,
                onEditClick = { onEditClick(item.task.uid) },
                onDeleteClick = { onDeleteClick(item.task.uid) }
            )
        }
    }
}

@Composable
fun TaskScreen(onCreateClick: () -> Unit, fabModifier: Modifier, onTaskEditClick: (Int) -> Unit,
               taskViewModel: TaskViewModel = viewModel()) {
    val tasksFlow = taskViewModel.tasksWithGameTitle

    val taskUidToDelete: MutableState<Int?> = remember { mutableStateOf(null) }

    val failureToast = Toast.makeText(LocalContext.current, stringResource(R.string.delete_failure_toast), Toast.LENGTH_SHORT)
    val resetDeleteState = { taskUidToDelete.value = null }

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

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.task_fab_add).uppercase()) },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                onClick = onCreateClick,
                modifier = fabModifier
            )
        }
    ) {
        tasksFlow.collectAsState(initial = listOf()).value.apply {
            TaskList(
                list = this,
                modifier = Modifier.padding(it),
                onEditClick = onTaskEditClick,
                onDeleteClick = { uid -> taskUidToDelete.value = uid }
            )
        }
    }
}
