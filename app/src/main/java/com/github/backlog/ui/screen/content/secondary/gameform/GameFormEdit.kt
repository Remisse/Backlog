package com.github.backlog.ui.screen.content.secondary.gameform

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.screen.ViewModelContainer
import com.github.backlog.viewmodel.GameViewModel

class GameFormEdit(private val onDialogSubmitClick: () -> Unit,
                   private val onSuccess: () -> Unit,
                   viewModelContainer: ViewModelContainer)
: BaseGameForm(viewModelContainer) {

    override val section: Section = Section.GameEdit

    @Composable
    override fun Content(arguments: Bundle?) {
        GameEditScreen(
            gameId = arguments?.getInt("gameId")!!,
            onDialogSubmitClick = onDialogSubmitClick,
            onSuccess = onSuccess,
            gameViewModel = viewModelContainer.gameViewModel
        )
    }
}

@Composable
private fun GameEditScreen(gameId: Int, onDialogSubmitClick: () -> Unit, onSuccess: () -> Unit,
                          gameViewModel: GameViewModel
) {
    var isGameFetched by rememberSaveable { mutableStateOf(false) }
    val formState = gameViewModel.formState

    if (!isGameFetched) {
        LaunchedEffect(Unit) {
            gameViewModel.entityById(gameId).collect {
                formState.fromEntity(it)
            }
            isGameFetched = true
        }
    }

    val successToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.game_edit_success), Toast.LENGTH_SHORT)
    val failureToast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.game_edit_fail_generic), Toast.LENGTH_SHORT)

    val context = LocalContext.current

    GameFormContent(
        state = formState,
        button = R.string.edit_button,
        onCancelDialogConfirm = onDialogSubmitClick,
        onCommitButtonClick = { state ->
            val errors = state.validateAll()
            if (errors.isEmpty()) {
                gameViewModel.update(
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
