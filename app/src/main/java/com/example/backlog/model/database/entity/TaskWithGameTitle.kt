package com.example.backlog.model.database.entity

import androidx.room.Embedded

data class TaskWithGameTitle(@Embedded val task: Task, val gameTitle: String)
