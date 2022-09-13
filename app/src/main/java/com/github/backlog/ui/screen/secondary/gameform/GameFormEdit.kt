package com.github.backlog.ui.screen.secondary.gameform

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.state.form.GameFormState
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor
import com.github.backlog.utils.errorToLocalizedString

class GameFormEdit(
    private val onCancelDialogConfirm: () -> Unit,
    private val onSuccess: () -> Unit,
    accessor: ViewModelContainerAccessor
) : BaseGameForm(accessor) {

    override val section: Section = Section.GameEdit

    @Composable
    override fun Content(arguments: Bundle?) {
        val formState = viewModelContainer().gameViewModel.formState

        LaunchedEffect(Unit) {
            viewModelContainer().gameViewModel.entityById(arguments?.getInt("gameId")!!)
                .collect {
                    formState.fromEntity(it)
                }
        }

        val successToast = Toast.makeText(
            LocalContext.current,
            stringResource(R.string.game_edit_success), Toast.LENGTH_SHORT)
        val failureToast = Toast.makeText(
            LocalContext.current,
            stringResource(R.string.game_edit_fail_generic), Toast.LENGTH_SHORT)

        val context = LocalContext.current

        GameEditScreen(
            onDialogSubmitClick = onCancelDialogConfirm,
            onCommitButtonClick = {
                val errors = formState.validateAll()
                if (errors.isEmpty()) {
                    viewModelContainer().gameViewModel.update(
                        entity = formState.toEntity(),
                        onSuccess = {
                            successToast.show()
                            onSuccess()
                        },
                        onFailure = { failureToast.show() }
                    )
                } else {
                    Toast.makeText(
                        context,
                        errorToLocalizedString(errors[0], context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            state = formState
        )
    }
}

@Composable
fun GameEditScreen(
    onDialogSubmitClick: () -> Unit,
    onCommitButtonClick: () -> Unit,
    state: GameFormState
) {
    GameFormContent(
        state = state,
        button = R.string.edit_button,
        onCancelDialogConfirm = onDialogSubmitClick,
        onCommitButtonClick = onCommitButtonClick
    )
}
