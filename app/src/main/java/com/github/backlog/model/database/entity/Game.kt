package com.github.backlog.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.backlog.model.GameStatus
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
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
