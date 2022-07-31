package com.github.backlog.ui.screen.content.secondary.taskform

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.screen.content.secondary.taskform.components.TaskFormContent
import com.github.backlog.util.AppContainer
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class TaskFormEdit(private val onDialogSubmitClick: () -> Unit,
                   private val onSuccess: () -> Unit,
                   appContainer: AppContainer
) : BaseTaskForm(appContainer) {

    override val section: Section = Section.TaskEdit

    @Composable
    override fun Content(arguments: Bundle?) {
        TaskEditContent(
            taskId = arguments?.getInt("taskId")!!,
            onEditSuccess = onSuccess,
            onCancelDialogSubmitClick = onDialogSubmitClick,
            gameViewModel = viewModelContainer.gameViewModel,
            taskViewModel = viewModelContainer.taskViewModel
        )
    }
}

@Composable
fun TaskEditContent(taskId: Int, onEditSuccess: () -> Unit, onCancelDialogSubmitClick: () -> Unit,
                   gameViewModel: GameViewModel, taskViewModel: TaskViewModel) {
    val state = taskViewModel.formState
    val isTaskImported = rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    if (!isTaskImported.value) {
        LaunchedEffect(Unit) {
            scope.launch {
                taskViewModel.entityById(taskId).collect {
                    state.fromEntity(it)

                    gameViewModel.entityById(it.gameId).collect { game ->
                        state.gameAndPlatform = "${game.title} (${game.platform})"
                    }

                    isTaskImported.value = true
                }
            }
        }
    }

    val successToast = Toast.makeText(
        LocalContext.current, stringResource(R.string.task_update_success),
        Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(
        LocalContext.current, stringResource(R.string.task_update_failure),
        Toast.LENGTH_SHORT)

    TaskFormContent(
        onSubmitClick = {
            if (it.validateAll().isEmpty()) {
                taskViewModel.update(
                    entity = it.toEntity(),
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
