package com.github.backlog.model.database.backlog.queryentity

import androidx.room.Embedded
import com.github.backlog.model.database.backlog.entity.Task

data class TaskWithGameTitle(@Embedded val task: Task, val gameTitle: String)
