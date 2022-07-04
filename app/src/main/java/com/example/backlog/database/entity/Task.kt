package com.example.backlog.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Game::class,
    parentColumns = ["uid"],
    childColumns = ["gameId"],
    onDelete = CASCADE)]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    val description: String,
    val gameId: Int,
    val deadline: Long?,
    val status: String
)