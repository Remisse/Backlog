package com.example.backlog.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey val uid: Int,
    val title: String,
    val platform: String,
    val status: String,
    @ColumnInfo(name = "cover_path") val coverPath: String?
)
