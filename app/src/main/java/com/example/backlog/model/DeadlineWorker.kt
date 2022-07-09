package com.example.backlog.model

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.time.LocalDate

/**
 * Since we can assume that deadlines won't be modified when the app is idle, this Worker will
 * use one static list provided by the caller in Data format.
 */
class DeadlineWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        val now = LocalDate.now().toEpochDay()
        val dueTasks = inputData.keyValueMap
            .filter { e -> (e.value as Long) - now > 0L }
            .toList()

        return Result.success(workDataOf(*dueTasks.toTypedArray()))
    }
}
