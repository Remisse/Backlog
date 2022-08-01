package com.github.backlog.model.network.adapter

import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class RawgPlatform(
    val id: Int,
    val slug: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class RawgRequirements(
    val minimum: String,
    val recommended: String
)

@JsonClass(generateAdapter = true)
data class RawgPlatformList(
    val platform: List<RawgPlatform>,
    val released_at: String?,
    val requirements: RawgRequirements?
)

@JsonClass(generateAdapter = true)
data class RawgEsrbRating(
    val id: Int,
    val slug: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class RawgGame(
    val id: Int,
    val slug: String,
    val name: String,
    val released: String,
    val tba: Boolean,
    val background_image: String,
    val rating: Int,
    val rating_top: Int,
    val ratings: List<Int>,
    val ratings_count: Int,
    val reviews_text_count: String,
    val added: Int,
    val added_by_status: Any,
    val metacritic: Int,
    val playtime: Int,
    val suggestions_count: Int,
    val updated: String,
    val esrb_rating: RawgEsrbRating?,
    val platforms: RawgPlatformList
)

@JsonClass(generateAdapter = true)
data class RawgList(val games: List<RawgGame>)

fun RawgList.asDomainModel(): List<Game> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd")

    return games.map {
        Game(
            title = it.name,
            // TODO Allow the user to choose the platform
            platform = it.platforms.platform.getOrNull(0)?.name.orEmpty(),
            status = GameStatus.NOT_STARTED,
            developer = "",
            publisher = "",
            genre = "",
            releaseDate = LocalDate.parse(it.released, formatter).toEpochDay(),
            coverPath = null
        )
    }
}
