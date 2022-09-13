package com.github.backlog.ui.screen.secondary.steam

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor

class SteamDialogScreen(
    private val onDismissRequest: () -> Unit,
    private val onConfirmClick: (String) -> Unit,
    accessor: ViewModelContainerAccessor
) : BaseScreen(accessor) {

    override val section: Section = Section.SteamImportPrep

    @Composable
    override fun Content(arguments: Bundle?) {
        SteamDialogContent(
            heading = stringResource(section.resourceId),
            onDismissRequest = onDismissRequest,
            onConfirmClick = onConfirmClick
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) { /* Empty */ }

    @Composable
    override fun TopBar() { /* Empty */ }

    @Composable
    override fun Fab() { /* Empty */ }
}
