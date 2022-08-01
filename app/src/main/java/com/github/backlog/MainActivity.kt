package com.github.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.backlog.ui.navigation.NavigationRoot
import com.github.backlog.ui.navigation.rememberNavigationState
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.util.ViewModelContainerAccessor

class MainActivity : ComponentActivity() {

    private lateinit var accessor: ViewModelContainerAccessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accessor = (application as BacklogApplication).viewModelContainerAccessor

        setContent {
            BacklogTheme {
                NavigationRoot(rememberNavigationState(appContainer = accessor))
            }
        }
    }
}
