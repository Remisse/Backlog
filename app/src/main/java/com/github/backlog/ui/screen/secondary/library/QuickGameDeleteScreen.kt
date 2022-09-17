package com.github.backlog.ui.screen.secondary.library

import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.components.DeleteDialog
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore

class QuickGameDeleteScreen(
    private val onDismissRequest: () -> Unit,
    private val onConfirmClick: () -> Unit,
    vmFactories: ViewModelFactoryStore
) : BaseScreen(vmFactories) {
    override val section: Section = Section.QuickGameDelete

    @Composable
    override fun Content(arguments: Bundle?) {
        val gameViewModel = gameViewModel()
        val uid: Int = arguments?.getInt("gameId")!!

        val successToast = Toast.makeText(
            LocalContext.current,
            stringResource(R.string.game_deleted_success_toast),
            Toast.LENGTH_SHORT
        )
        val failureToast = Toast.makeText(
            LocalContext.current,
            stringResource(R.string.delete_failure_toast),
            Toast.LENGTH_SHORT
        )

        DeleteDialog(
            onDismissRequest = onDismissRequest,
            onConfirmDeleteClick = {
                gameViewModel.delete(
                    uid = uid,
                    onSuccess = {
                        successToast.show()
                        onConfirmClick()
                    },
                    onFailure = {
                        failureToast.show()
                        onDismissRequest()
                    }
                )
            },
            onCancelClick = onDismissRequest,
            body = R.string.game_delete_dialog_body
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) {}

    @Composable
    override fun TopBar() {}

    @Composable
    override fun Fab() {}
}
