package com.github.backlog.model.database.backlog.entity

import androidx.room.Embedded

data class TaskWithGameTitle(@Embedded val task: Task, val gameTitle: String)
