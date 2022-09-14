package com.github.backlog.ui.screen.secondary.onlinesearch

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.github.backlog.Section
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.components.ErrorDialog
import com.github.backlog.ui.screen.BaseScreen
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class OnlineSearchScreen(
    private val onBackClick: () -> Unit,
    private val onGameClick: (String) -> Unit,
    vmFactories: ViewModelFactoryStore
) : BaseScreen(vmFactories) {
    private var searchQuery by mutableStateOf("")
    private var searchResults: Flow<List<Game>> = emptyFlow()
    private var searchPerformed by mutableStateOf(false)

    override val section: Section = Section.OnlineSearch

    @Composable
    override fun Content(arguments: Bundle?) {
        val onlineSearchViewModel = onlineSearchViewModel()

        if (onlineSearchViewModel.isNetworkError && !onlineSearchViewModel.isErrorShown) {
            ErrorDialog(
                errorMessage = stringResource(R.string.error_generic),
                onConfirmClick = { onlineSearchViewModel.isErrorShown = true },
                onDismissRequest = { onlineSearchViewModel.isErrorShown = true }
            )
        }
        OnlineSearchContent(
            onGameClick = onGameClick,
            results = searchResults,
            searchPerformed = searchPerformed
        )
    }

    @Composable
    override fun BottomBar(navController: NavHostController, sections: List<Section>) { }

    @Composable
    override fun TopBar() {
        val onlineSearchViewModel = onlineSearchViewModel()
        val scope = rememberCoroutineScope()

        TopSearchBar(
            searchValue = searchQuery,
            onValueChange = { searchQuery = it },
            onBackClick = onBackClick,
            onSearchSubmit = {
                scope.launch {
                    searchPerformed = false
                    searchResults = onlineSearchViewModel.performSearch(searchQuery)
                    searchPerformed = true
                }
            }
        )
    }

    @Composable
    override fun Fab() { }
}
