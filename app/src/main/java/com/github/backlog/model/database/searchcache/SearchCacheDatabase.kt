package com.github.backlog.model.database.searchcache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.backlog.model.database.backlog.dao.GameDao
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.dao.SearchDao
import com.github.backlog.model.database.searchcache.entity.Search

@Database(
    entities = [Game::class, Search::class],
    version = 1
)
abstract class SearchCacheDatabase : RoomDatabase() {

    abstract fun gamedao(): GameDao

    abstract fun searchdao(): SearchDao
}