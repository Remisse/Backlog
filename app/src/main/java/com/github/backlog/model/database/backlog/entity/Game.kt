package com.github.backlog.model.database.backlog.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.backlog.model.GameStatus
import com.squareup.moshi.JsonClass

// Impose a UNIQUE constraint on 'steam_id' and 'rawg_id' through indexes
@Entity(
    indices = [
        Index(value = ["steam_id"], unique = true),
        Index(value = ["rawg_id"], unique = true)
    ]
)
@JsonClass(generateAdapter = true)
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "steam_id") val steamId: String? = null,
    @ColumnInfo(name = "rawg_id") val rawgId: String? = null,
    val title: String,
    val platform: String? = null,
    val status: GameStatus,
    val developer: String? = null,
    val publisher: String? = null,
    val genre: String? = null,
    @ColumnInfo(name = "completion_date") val completionDate: Long? = null,
    @ColumnInfo(name = "cover_path") val coverPath: String? = null
)
