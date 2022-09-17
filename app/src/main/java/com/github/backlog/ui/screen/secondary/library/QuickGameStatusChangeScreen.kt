package com.github.backlog.ui.screen.secondary.library

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.model.GameStatus
import com.github.backlog.ui.components.*
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.utils.now

class QuickStatusChangeDialogScreen(
    private val onDismissRequest: () -> Unit,
    private val onStatusSelect: () -> Unit,
    vmFactories: ViewModelFactoryStore
) : BaseScreen(vmFactories) {
    override val section: Section = Section.QuickGameStatusChange

    @Composable
    override fun Content(arguments: Bundle?) {
        val gameViewModel = gameViewModel()
        val uid = arguments?.getInt("gameId")!!

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            StatusChangeContent(
                heading = stringResource(section.resourceId),
                onStatusSelect = {
                    gameViewModel.setStatus(uid, it)
                    if (it == GameStatus.COMPLETED) {
                        gameViewModel.setCompletionDate(uid, now())
                    }
                    onStatusSelect()
                },
                onDismissRequest = onDismissRequest
            )
        }
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) {}

    @Composable
    override fun TopBar() {}

    @Composable
    override fun Fab() {}
}

@Composable
private fun StatusChangeContent(
    heading: String,
    onStatusSelect: (GameStatus) -> Unit,
    onDismissRequest: () -> Unit
) {
    Surface(shape = LookAndFeel.DialogSurfaceShape, modifier = Modifier.padding(4.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StatusMenu(
                expanded = true,
                onSelect = onStatusSelect,
                onDismissRequest = onDismissRequest,
                toColor = { it.toColor() },
                toResource = { it.toResource() },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            )
        }

    }
}
