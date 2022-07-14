package com.example.backlog.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.backlog.model.GameStatus
import com.google.gson.annotations.SerializedName

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @SerializedName("title") val title: String,
    @SerializedName("platform") val platform: String,
    val status: GameStatus,
    @SerializedName("developer") val developer: String?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("release_date") @ColumnInfo(name = "release_date") val releaseDate: Long?,
    @ColumnInfo(name = "cover_path") val coverPath: String?
)
