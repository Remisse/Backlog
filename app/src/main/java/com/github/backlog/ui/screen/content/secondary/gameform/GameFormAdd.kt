package com.github.backlog.ui.screen.content.secondary.gameform

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.backlog.R
import com.github.backlog.Section
import com.github.backlog.util.AppContainer
import com.github.backlog.viewmodel.GameViewModel

class GameFormAdd(private val onDialogSubmitClick: () -> Unit,
                  private val onSuccess: () -> Unit,
                  appContainer: AppContainer)
: BaseGameForm(appContainer) {

    override val section: Section = Section.GameAdd

    @Composable
    override fun Content(arguments: Bundle?) {
        GameAddScreen(
            onDialogSubmitClick = onDialogSubmitClick,
            onSuccess = onSuccess,
            gameViewModel = viewModelContainer.gameViewModel
        )
    }
}

@Composable
private fun GameAddScreen(onDialogSubmitClick: () -> Unit, onSuccess: () -> Unit,
                           gameViewModel: GameViewModel) {
    val formState = gameViewModel.formState

    val successToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.insert_game_success_toast), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.insert_game_failure_toast), Toast.LENGTH_SHORT)

    val context = LocalContext.current

    GameFormContent(
        state = formState,
        button = R.string.insert_button_add,
        onCancelDialogConfirm = onDialogSubmitClick,
        onCommitButtonClick = { state ->
            val errors = state.validateAll()
            if (errors.isEmpty()) {
                gameViewModel.insert(
                    entity = formState.toEntity(),
                    onSuccess = {
                        successToast.show()
                        onSuccess()
                    },
                    onFailure = { failureToast.show() }
                )
            } else {
                Toast.makeText(context, errors[0], Toast.LENGTH_SHORT).show()
            }
        }
    )
}
