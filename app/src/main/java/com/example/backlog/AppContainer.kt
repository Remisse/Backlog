package com.example.backlog

import com.example.backlog.database.BacklogDatabase
import com.example.backlog.viewmodel.factory.GameViewModelFactory

class AppContainer(private val database: BacklogDatabase) {

    val gameViewModelFactory = GameViewModelFactory(database.gamedao())
}