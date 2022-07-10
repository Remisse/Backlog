package com.example.backlog.model.database

import androidx.room.TypeConverter
import com.example.backlog.model.GameStatus
import com.example.backlog.model.TaskStatus

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
