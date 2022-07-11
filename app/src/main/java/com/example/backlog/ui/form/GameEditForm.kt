package com.example.backlog.ui.form

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.backlog.R
import com.example.backlog.ui.state.GameFormState
import com.example.backlog.viewmodel.GameViewModel

@Composable
fun GameEditScreen(gameId: Int, onEditSuccess: () -> Unit, onCancelDialogSubmitClick: () -> Unit,
                   gameViewModel: GameViewModel) {
    val state = remember { GameFormState() }
    val isGameImported = rememberSaveable { mutableStateOf(false) }

    if (!isGameImported.value) {
        LaunchedEffect(Unit) {
            gameViewModel.entityById(gameId).collect {
                state.fromEntity(it)
                isGameImported.value = true
            }
        }
    }

    val successToast =
        Toast.makeText(LocalContext.current, stringResource(R.string.game_edit_success), Toast.LENGTH_SHORT)
    val failureToast =
        Toast.makeText(LocalContext.current, stringResource(R.string.game_edit_fail_generic), Toast.LENGTH_SHORT)

    GameFormContent(
        state = state,
        button = R.string.edit_button,
        onCancelDialogConfirm = onCancelDialogSubmitClick,
        onCommitButtonClick = {
            if (state.validateAll()) {
                gameViewModel.update(
                    entity = state.toEntity(),
                    onSuccess = {
                        successToast.show()
                        onEditSuccess()
                    },
                    onFailure = {
                        failureToast.show()
                    }
                )
            }
        }
    )
}
