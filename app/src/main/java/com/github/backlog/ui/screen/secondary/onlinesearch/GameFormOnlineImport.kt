package com.github.backlog.ui.screen.secondary.onlinesearch

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.ErrorDialog
import com.github.backlog.ui.screen.secondary.gameform.BaseGameForm
import com.github.backlog.ui.screen.secondary.gameform.GameAddScreen
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.utils.errorToLocalizedString
import kotlinx.coroutines.launch

class GameFormOnlineImport(
    private val onCancelDialogConfirm: () -> Unit,
    private val onSuccess: () -> Unit,
    private val onNetworkErrorAcknowledge: () -> Unit,
    vmFactories: ViewModelFactoryStore
) : BaseGameForm(vmFactories) {

    override val section = Section.OnlineImport

    @Composable
    override fun Content(arguments: Bundle?) {
        val gameViewModel = gameViewModel()
        val onlineSearchViewModel = onlineSearchViewModel()

        val detailedGame: MutableState<Game?> = remember { mutableStateOf(null) }
        val formState = gameViewModel.formState
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            scope.launch {
                onlineSearchViewModel
                    .retrieveGameDetails(arguments?.getString("gameId")!!)
                    .collect {
                        detailedGame.value = it
                        formState.fromEntity(it)
                    }
            }
        }

        if (onlineSearchViewModel.isNetworkError && !onlineSearchViewModel.isErrorShown) {
            ErrorDialog(
                errorMessage = stringResource(R.string.error_generic),
                onConfirmClick = {
                    onlineSearchViewModel.isErrorShown = true
                    onNetworkErrorAcknowledge()
                },
                onDismissRequest = { /* Empty */ }
            )
        }

        if (detailedGame.value == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.online_form_loading),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                CircularProgressIndicator()
            }
        } else {
            val successToast = Toast.makeText(
                LocalContext.current,
                stringResource(R.string.online_import_success), Toast.LENGTH_SHORT)
            val failureToast = Toast.makeText(
                LocalContext.current,
                stringResource(R.string.online_import_fail), Toast.LENGTH_SHORT)

            val context = LocalContext.current

            GameAddScreen(
                onDialogSubmitClick = onCancelDialogConfirm,
                onCommitButtonClick = {
                    val errors = formState.validateAll()
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
}
