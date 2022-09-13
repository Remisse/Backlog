package com.github.backlog.utils

import com.github.backlog.viewmodel.GameViewModel
import com.github.backlog.viewmodel.OnlineSearchViewModel
import com.github.backlog.viewmodel.ProfileViewModel
import com.github.backlog.viewmodel.TaskViewModel

// TODO Delete and refactor everything using Lifecycles
data class ViewModelContainer(
    val gameViewModel: GameViewModel,
    val taskViewModel: TaskViewModel,
    val onlineSearchViewModel: OnlineSearchViewModel,
    val profileViewModel: ProfileViewModel
)
