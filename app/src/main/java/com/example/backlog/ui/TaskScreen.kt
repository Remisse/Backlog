package com.example.backlog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.backlog.R
import com.example.backlog.database.entity.TaskWithGameTitle
import com.example.backlog.viewmodel.TaskViewModel
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
private fun TaskList(list: List<TaskWithGameTitle>, modifier: Modifier) {
    val iconModifier = Modifier.size(12.dp)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(list) { item ->
            ItemCard(
                topText = { Text(
                    text = "${item.gameTitle}: ${item.task.description}",
                    fontWeight = FontWeight.Bold
                ) },
                subText = listOf {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                        Text(
                            text = LocalDate.ofEpochDay((item.task.deadlineDateEpochDay) as Long).toString(),
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                        Text(LocalTime.ofSecondOfDay(item.task.deadlineTimeSeconds?.toLong()!!).toString())
                    }
                },
                hiddenText = listOf())
        }
    }
}

@Composable
fun TaskScreen(onCreateClick: () -> Unit, fabModifier: Modifier, taskViewModel: TaskViewModel) {
    val tasks = taskViewModel.tasksWithGameTitle.observeAsState()

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
        TaskList(list = tasks.value.orEmpty(), modifier = Modifier.padding(it))
    }
}
