package com.github.backlog.model.database.searchcache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.backlog.model.Converters
import com.github.backlog.model.database.backlog.dao.GameDao
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.searchcache.dao.RawgDao
import com.github.backlog.model.database.searchcache.dao.SearchDao
import com.github.backlog.model.database.searchcache.dao.SteamDao
import com.github.backlog.model.database.searchcache.entity.GameSlim
import com.github.backlog.model.database.searchcache.entity.Search

@Database(
    entities = [Game::class, Search::class, GameSlim::class],
    version = 11
)
@TypeConverters(Converters::class)
abstract class SearchCacheDatabase : RoomDatabase() {

    abstract fun searchdao(): SearchDao

    abstract fun gamedao(): GameDao

    abstract fun rawgdao(): RawgDao

    abstract fun steamdao(): SteamDao
}
