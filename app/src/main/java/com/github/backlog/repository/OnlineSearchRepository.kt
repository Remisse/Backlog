package com.github.backlog.repository

import android.util.Log
import androidx.room.withTransaction
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.database.searchcache.entity.GameSlim
import com.github.backlog.model.database.searchcache.entity.Search
import com.github.backlog.model.onlineservice.OnlineSearchService
import com.github.backlog.model.onlineservice.adapter.asBacklogDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.net.URLEncoder
import java.time.Clock
import java.time.Instant

private const val ONE_DAY_SECONDS = 86400

class OnlineSearchRepository(
    private val database: SearchCacheDatabase,
    private val service: OnlineSearchService
) {
    private suspend fun isSearchOutdated(query: String): Boolean {
        var isSearchOutdated = true

        database.searchdao()
            .getSearchTimestamp(query)
            .firstOrNull()
            ?.let {
                isSearchOutdated =
                    Instant.now(Clock.systemDefaultZone()).epochSecond - it > ONE_DAY_SECONDS
            }

        return isSearchOutdated
    }

    suspend fun cacheRawgSearch(query: String) {
        withContext(Dispatchers.IO) {
            if (isSearchOutdated(query)) {
                // This warning is a false positive. See https://stackoverflow.com/a/69070736.
                val queryEncoded = URLEncoder.encode(query, "utf-8")
                val results = service.searchOnRawg(queryEncoded)

                val games = results.map { it.asBacklogDatabaseModel() }

                database.withTransaction {
                    database.searchdao().insertSearch(Search(query = query, timestamp = Instant.now().epochSecond))
                    database.gamedao().insertAll(games)
                    database.rawgdao().insertGamesBySearch(games.map { GameSlim(
                        gameId = it.rawgId!!,
                        search = query,
                        isDetailed = false) }
                    )
                }
            }
        }
    }

    fun getGamesByRawgSearch(query: String): Flow<List<Game>> {
        return database.rawgdao().gamesBySearch(query)
    }

    suspend fun getGameDetailsFromRawg(rawgId: String): Game {
        return withContext(Dispatchers.IO) {
            return@withContext service.rawgDetails(rawgId)

        }.asBacklogDatabaseModel()
        .withZeroUid()
    }

    suspend fun cacheSteamImport(steamId: String) {
        withContext(Dispatchers.IO) {
            if (isSearchOutdated(steamId)) {
                // This warning is a false positive. See https://stackoverflow.com/a/69070736.
                val queryEncoded = URLEncoder.encode(steamId, "utf-8")
                val results = service.steamLibrary(steamId)
                val games = results.map { it.asBacklogDatabaseModel() }

                database.withTransaction {
                    database.searchdao().insertSearch(Search(query = steamId, timestamp = Instant.now().epochSecond))
                    database.gamedao().insertAll(games)
                    database.steamdao().insertGamesByAccount(games.map { GameSlim(
                        gameId = it.steamId!!,
                        search = steamId,
                        isDetailed = false) }
                    )
                }
            }
        }
    }

    fun getGamesBySteamImport(steamId: String): Flow<List<Game>> {
        return database.steamdao().gamesByAccount(steamId)
    }
}

private fun Game.withZeroUid(): Game {
    return Game(
        uid = 0,
        steamId = this.steamId,
        rawgId = this.rawgId,
        title = this.title,
        platform = this.platform,
        status = this.status,
        developer = this.developer,
        publisher = this.publisher,
        genre = this.genre,
        completionDate = this.completionDate,
        coverPath = this.coverPath
    )
}