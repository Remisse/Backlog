package com.example.backlog

import com.example.backlog.model.BacklogDatabase
import com.example.backlog.viewmodel.GameViewModelFactory

class AppContainer(database: BacklogDatabase) {

    val gameViewModelFactory = GameViewModelFactory(database.gamedao())
}