package com.github.backlog.model.database.searchcache.entity

import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Search::class,
            parentColumns = ["query"],
            childColumns = ["search"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["game_id", "search"], unique = true)
    ]
)
data class GameSlim(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "game_id") val gameId: String, // Steam App ID or RAWG ID, depending on context
    val search: String,
    @ColumnInfo(name = "is_detailed") val isDetailed: Boolean
)
