package com.github.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.util.AppContainer
import com.github.backlog.ui.NavigationRoot

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = (application as BacklogApplication).appContainer

        setContent {
            val appState = rememberBacklogAppState(appContainer = appContainer)

            BacklogTheme {
                NavigationRoot(appState)
            }
        }
    }
}
