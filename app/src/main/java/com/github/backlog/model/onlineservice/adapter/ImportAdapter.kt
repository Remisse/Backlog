package com.github.backlog.model.onlineservice.adapter

import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.squareup.moshi.JsonClass
import java.lang.IllegalStateException

@JsonClass(generateAdapter = true)
data class GameJson(
    val steam_id: String,
    val rawg_id: String,
    val title: String
)

@JsonClass(generateAdapter = true)
data class GameDetailed(
    val steam_id: String?,
    val rawg_id: String?,
    val title: String,
    val developer: String?,
    val publisher: String?,
    val genre: String?
)

fun GameJson.asBacklogDatabaseModel(): Game {
    if (this.steam_id == "" && this.rawg_id == "") {
        throw IllegalStateException("Steam App ID and RAWG ID cannot both be empty.")
    }
    return Game(
        steamId = this.steam_id.takeIf { it != "" },
        rawgId = this.rawg_id.takeIf { it != "" },
        title = this.title,
        platform = if (this.steam_id != "") "PC" else null,
        status = GameStatus.NOT_STARTED,
        developer = null,
        publisher = null,
        genre = null,
        completionDate = null,
        coverPath = null
    )
}

fun GameDetailed.asBacklogDatabaseModel(): Game {
    if (this.steam_id == "" && this.rawg_id == "") {
        throw IllegalStateException("Steam App ID and RAWG ID cannot both be empty.")
    }
    return Game(
        steamId = this.steam_id.takeIf { it != "" },
        rawgId = this.rawg_id.takeIf { it != "" },
        title = this.title,
        platform = if (this.steam_id != "") "PC" else null,
        status = GameStatus.NOT_STARTED,
        developer = this.developer.takeIf { it != "" },
        publisher = this.publisher.takeIf { it != "" },
        genre = this.genre.takeIf { it != "" },
        completionDate = null,
        coverPath = null
    )
}
