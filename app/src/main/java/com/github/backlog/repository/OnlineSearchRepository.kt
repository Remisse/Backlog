package com.github.backlog.repository

import com.github.backlog.model.database.searchcache.SearchCacheDatabase
import com.github.backlog.model.network.OnlineSearchService
import com.github.backlog.model.network.adapter.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.withContext

class OnlineSearchRepository(private val database: SearchCacheDatabase,
                             private val service: OnlineSearchService
) {

    suspend fun doSearch(query: String) {
        withContext(Dispatchers.IO) {
            val cache = database.searchdao().cachedGamesFromSearch(query)
            if (cache.count() == 0) {
                val results = service.searchGame(query)
                val games = results.asDomainModel()


            }
        }
    }
}
