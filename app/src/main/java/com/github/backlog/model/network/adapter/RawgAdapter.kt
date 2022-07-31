package com.github.backlog.model.network.adapter

import com.squareup.moshi.JsonClass

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
