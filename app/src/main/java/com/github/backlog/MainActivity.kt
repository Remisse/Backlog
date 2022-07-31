package com.github.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.util.ViewModelContainerAccessor
import com.github.backlog.ui.navigation.NavigationRoot
import com.github.backlog.ui.navigation.rememberNavigationState

class MainActivity : ComponentActivity() {

    private lateinit var accessor: ViewModelContainerAccessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accessor = (application as BacklogApplication).appContainer

        setContent {
            val appState = rememberNavigationState(appContainer = accessor)

            BacklogTheme {
                NavigationRoot(appState)
            }
        }
    }
}
