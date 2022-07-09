package com.example.backlog.ui.form

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backlog.R
import com.example.backlog.viewmodel.GameViewModel
import com.example.backlog.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun TaskEditScreen(taskId: Int, onEditSuccess: () -> Unit, onCancelDialogSubmitClick: () -> Unit,
                   gameViewModel: GameViewModel = viewModel(),
                   taskViewModel: TaskViewModel = viewModel()) {
    val task = taskViewModel.taskById(taskId)
    val state = taskViewModel.formState
    val isTaskImported = rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    if (!isTaskImported.value) {
        LaunchedEffect(Unit) {
            scope.launch {
                task.collect {
                    state.fromEntity(it)

                    gameViewModel.gameById(it.gameId).collect { game ->
                        state.gameAndPlatform = "${game.title} (${game.platform})"
                    }

                    isTaskImported.value = true
                }
            }
        }
    }

    val successToast = Toast.makeText(LocalContext.current, stringResource(R.string.task_update_success),
        Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(LocalContext.current, stringResource(R.string.task_update_failure),
        Toast.LENGTH_SHORT)

    TaskFormContent(
        onSubmitClick = {
            if (it.validateAll()) {
                taskViewModel.update(
                    task = it.toEntity(),
                    onSuccess = {
                        successToast.show()
                        onEditSuccess()
                    },
                    onFailure = {
                        failureToast.show()
                    }
                )
            }
        },
        onCancelDialogSubmit = onCancelDialogSubmitClick,
        state = state,
        topBarTitle = R.string.task_update_heading,
        submitButton = R.string.edit_button,
        gameViewModel = gameViewModel
    )
}
