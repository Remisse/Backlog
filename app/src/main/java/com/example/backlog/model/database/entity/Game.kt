package com.example.backlog.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.backlog.model.GameStatus
import java.time.LocalDate

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val platform: String,
    val status: GameStatus,
    val developer: String?,
    val publisher: String?,
    val genre: String?,
    @ColumnInfo(name = "release_date") val releaseDate: Long?,
    @ColumnInfo(name = "cover_path") val coverPath: String?
)
