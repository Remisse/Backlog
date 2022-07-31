package com.github.backlog.model.database

import androidx.room.TypeConverter
import com.github.backlog.model.GameStatus
import com.github.backlog.model.TaskStatus

class Converters {

    @TypeConverter
    fun toTaskStatus(value: String) = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus) = value.name

    @TypeConverter
    fun toGameStatus(value: String): GameStatus = GameStatus.valueOf(value)

    @TypeConverter
    fun fromGameStatus(value: GameStatus): String = value.name
}
