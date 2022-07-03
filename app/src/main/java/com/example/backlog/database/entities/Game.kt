package com.example.backlog.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val platform: String,
    val status: String,
    @ColumnInfo(name = "cover_path") val coverPath: String?
)
