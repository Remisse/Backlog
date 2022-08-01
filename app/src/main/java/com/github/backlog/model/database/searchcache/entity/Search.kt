package com.github.backlog.model.database.searchcache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Search(
    @PrimaryKey val query: String,
    val timestamp: Long
)
