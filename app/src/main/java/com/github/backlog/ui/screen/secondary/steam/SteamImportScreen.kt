package com.github.backlog.ui.screen.secondary.steam

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
import androidx.navigation.NavHostController
import com.github.backlog.R
import com.github.backlog.Section
import com.github.backlog.ui.components.ErrorDialog
import com.github.backlog.ui.components.SubScreenTopBar
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore

class SteamImportScreen(
    private val onBackClick: () -> Unit,
    private val onSuccess: () -> Unit,
    private val onNetworkErrorAcknowledge: () -> Unit,
    vmFactories: ViewModelFactoryStore
) : BaseScreen(vmFactories) {

    override val section: Section = Section.SteamImport

    @Composable
    override fun Content(arguments: Bundle?) {
        val onlineSearchViewModel = onlineSearchViewModel()
        val gameViewModel = gameViewModel()

        val steamId = arguments?.getString("steamId")!!
        val games by onlineSearchViewModel.retrieveSteamLibrary(steamId)
            .collectAsState(initial = emptyList())

        if (games.isEmpty() && !onlineSearchViewModel.isNetworkError) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.steam_library_wait),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                CircularProgressIndicator()
            }
        } else if (onlineSearchViewModel.isNetworkError && !onlineSearchViewModel.isErrorShown
        ) {
            ErrorDialog(
                errorMessage = stringResource(R.string.steam_import_prep_error),
                onConfirmClick = {
                    onlineSearchViewModel.isErrorShown = true
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
                    gameViewModel.insertAll(
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
