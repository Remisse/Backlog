package com.github.backlog.ui.screen.secondary.steam

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.components.ErrorDialog
import com.github.backlog.ui.components.SubScreenTopBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor

class SteamImportScreen(
    private val onBackClick: () -> Unit,
    private val onSuccess: () -> Unit,
    private val onNetworkErrorAcknowledge: () -> Unit,
    accessor: ViewModelContainerAccessor
) : BaseScreen(accessor) {

    override val section: Section = Section.SteamImport

    @Composable
    override fun Content(arguments: Bundle?) {
        val steamId = arguments?.getString("steamId")!!
        val games by viewModelContainer().onlineSearchViewModel.retrieveSteamLibrary(steamId)
            .collectAsState(initial = emptyList())

        if (games.isEmpty() && !viewModelContainer().onlineSearchViewModel.isNetworkError) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.steam_library_wait),
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                CircularProgressIndicator()
            }
        } else if (viewModelContainer().onlineSearchViewModel.isNetworkError
            && !viewModelContainer().onlineSearchViewModel.isErrorShown
        ) {
            ErrorDialog(
                errorMessage = stringResource(R.string.steam_import_prep_error),
                onConfirmClick = {
                    viewModelContainer().onlineSearchViewModel.isErrorShown = true
                    onNetworkErrorAcknowledge()
                },
                onDismissRequest = { /* Empty */}
            )
        } else {
            val successToast = Toast.makeText(
                LocalContext.current,
                stringResource(R.string.steam_import_success),
                Toast.LENGTH_SHORT
            )
            val failureToast = Toast.makeText(
                LocalContext.current,
                stringResource(R.string.steam_import_fail),
                Toast.LENGTH_SHORT
            )

            SteamImportContent(
                games = games.sortedBy { it.title },
                onConfirmClick =  {
                    viewModelContainer().gameViewModel.insertAll(
                        entities = it.toList(),
                        onSuccess = {
                            successToast.show()
                            onSuccess()
                        },
                        onFailure = { failureToast.show() }
                    )
                }
            )
        }
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) { }

    @Composable
    override fun TopBar() {
        SubScreenTopBar(
            heading = R.string.steam_import_heading,
            onBackClick = onBackClick
        )
    }

    @Composable
    override fun Fab() { }
}
