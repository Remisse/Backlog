package com.github.backlog.ui.screen.secondary.taskform

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor
import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.TaskViewModel

class TaskFormAdd(private val onDialogSubmitClick: () -> Unit,
                  private val onSuccess: () -> Unit,
                  accessor: ViewModelContainerAccessor
) : BaseTaskForm(accessor) {

    override val section: Section = Section.TaskAdd

    @Composable
    override fun Content(arguments: Bundle?) {
        TaskAddContent(
            onEntryAddSuccess = onSuccess,
            onCancelDialogSubmit = onDialogSubmitClick,
            gameViewModel = viewModelContainer().gameViewModel,
            taskViewModel = viewModelContainer().taskViewModel
        )
    }
}

@Composable
fun TaskAddContent(onEntryAddSuccess: () -> Unit, onCancelDialogSubmit: () -> Unit,
                   gameViewModel: GameViewModel, taskViewModel: TaskViewModel
) {
    val formState = taskViewModel.formState

    val successToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.task_insert_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.task_insert_failure_toast), Toast.LENGTH_SHORT)

    TaskFormContent(
        onSubmitClick = {
            if (it.validateAll().isEmpty()) {
                taskViewModel.insert(
                    entity = it.toEntity(),
                    onSuccess = {
                        successToast.show()
                        onEntryAddSuccess()
                    },
                    onFailure = {
                        failureToast.show()
                    }
                )
            }
        },
        onCancelDialogSubmit = onCancelDialogSubmit,
        state = formState,
        topBarTitle = R.string.task_fab_add,
        submitButton = R.string.task_fab_add,
        gameViewModel = gameViewModel
    )
}
