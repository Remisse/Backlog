package com.github.backlog.model.database.searchcache.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.github.backlog.model.database.backlog.entity.Game

@Entity(
    primaryKeys = ["gameId", "search"],
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["uid"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Search::class,
            parentColumns = ["query"],
            childColumns = ["search"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameSearch(
    val gameId: Int,
    val search: String
)
