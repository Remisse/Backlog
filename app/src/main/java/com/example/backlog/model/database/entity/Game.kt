package com.example.backlog.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.backlog.model.database.GameStatus

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val platform: String,
    val status: GameStatus,
    @ColumnInfo(name = "retail_price") val retailPrice: Long,
    @ColumnInfo(name = "cover_path") val coverPath: String?
)
