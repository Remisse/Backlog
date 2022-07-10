package com.example.backlog.ui

import androidx.compose.ui.graphics.Color
import com.example.backlog.R
import com.example.backlog.model.GameStatus
import com.example.backlog.model.TaskStatus
import com.example.backlog.ui.theme.*

fun taskStatusToResource(status: TaskStatus): Int {
    return when (status) {
        TaskStatus.IN_PROGRESS -> R.string.task_status_in_progress
        TaskStatus.COMPLETED -> R.string.task_status_completed
        TaskStatus.FAILED -> R.string.task_status_failed
    }
}

fun taskStatusToColor(status: TaskStatus): Color {
    return when (status) {
        TaskStatus.IN_PROGRESS -> YellowTetrad1
        TaskStatus.COMPLETED -> TealTetrad1
        TaskStatus.FAILED -> OrangeTetrad1
    }
}

fun gameStatusToResource(status: GameStatus): Int {
    return when (status) {
        GameStatus.PLAYING -> R.string.status_playing
        GameStatus.NOT_STARTED -> R.string.status_not_started
        GameStatus.COMPLETED -> R.string.status_completed
        GameStatus.DROPPED -> R.string.status_dropped
    }
}

fun gameStatusToColor(status: GameStatus): Color {
    return when (status) {
        GameStatus.NOT_STARTED -> CyanAdjacent1
        GameStatus.PLAYING -> BlueAdjacent1
        GameStatus.COMPLETED -> GreenAdjacent1
        GameStatus.DROPPED -> OrangeAdjacent1Complementary
    }
}