package com.example.backlog.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.backlog.viewmodel.TaskViewModel



@Composable
fun TaskCreationScreen(onEntryAddSuccess: () -> Unit, onDialogSubmitClick: () -> Unit,
                       taskViewModel: TaskViewModel) {
    Scaffold(
        topBar = {
            GameCreationTopBar {

            }
        }
    ) {
        Foo(it)
    }
}