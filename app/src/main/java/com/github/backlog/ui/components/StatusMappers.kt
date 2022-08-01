package com.github.backlog.ui.components

import androidx.compose.ui.graphics.Color
import com.example.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.TaskStatus
import com.github.backlog.ui.theme.*

fun TaskStatus.toResource(): Int {
    return when (this) {
        TaskStatus.IN_PROGRESS -> R.string.task_status_in_progress
        TaskStatus.COMPLETED -> R.string.task_status_completed
        TaskStatus.FAILED -> R.string.task_status_failed
    }
}

fun TaskStatus.toColor(): Color {
    return when (this) {
        TaskStatus.IN_PROGRESS -> YellowTetrad1
        TaskStatus.COMPLETED -> TealTetrad1
        TaskStatus.FAILED -> OrangeTetrad1
    }
}

fun GameStatus.toResource(): Int {
    return when (this) {
        GameStatus.PLAYING -> R.string.status_playing
        GameStatus.NOT_STARTED -> R.string.status_not_started
        GameStatus.COMPLETED -> R.string.status_completed
        GameStatus.DROPPED -> R.string.status_dropped
    }
}

fun GameStatus.toColor(): Color {
    return when (this) {
        GameStatus.NOT_STARTED -> CyanAdjacent1
        GameStatus.PLAYING -> BlueAdjacent1
        GameStatus.COMPLETED -> GreenAdjacent1
        GameStatus.DROPPED -> OrangeAdjacent1Complementary
    }
}
