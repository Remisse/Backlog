package com.example.backlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.backlog.ui.NavigationSystem

class MainActivity : ComponentActivity() {

    private lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        container = (application as BacklogApplication).appContainer
        setContent {
            NavigationSystem(container)
        }
    }
}