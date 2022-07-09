package com.example.backlog.model.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.backlog.model.database.TaskStatus

@Entity(foreignKeys = [ForeignKey(entity = Game::class,
    parentColumns = ["uid"],
    childColumns = ["gameId"],
    onDelete = CASCADE)]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val description: String,
    val gameId: Int,
    val deadlineDateEpochDay: Long?,
    val status: TaskStatus
)