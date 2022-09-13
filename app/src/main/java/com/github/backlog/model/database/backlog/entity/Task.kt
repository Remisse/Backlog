package com.github.backlog.model.database.backlog.entity

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.github.backlog.model.TaskStatus

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["uid"],
            childColumns = ["game_id"],
            onDelete = CASCADE)
    ],
    indices = [
        Index(value = ["game_id"], unique = false)
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val description: String,
    @ColumnInfo(name = "game_id") val gameId: Int,
    val deadline: Long?,
    val status: TaskStatus,
    val notified: Boolean = false
)
