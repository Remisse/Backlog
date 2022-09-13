package com.github.backlog.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.github.backlog.R
import com.github.backlog.model.DeadlineWorker
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.dao.TaskDao
import com.github.backlog.model.database.backlog.entity.Task
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import com.github.backlog.ui.state.form.TaskFormState
import com.github.backlog.utils.localDateFromEpochSecond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

private const val WORK_TAG = "deadline"

class TaskViewModel(
    private val dao: TaskDao,
    private val workManager: WorkManager,
    private val notificationBuilder: NotificationCompat.Builder
) : ViewModel() {
    val formState = TaskFormState()

    val tasksWithGameTitle: Flow<List<TaskWithGameTitle>> = dao.tasksByGame()

    private var workerId: UUID? = null

    @SuppressLint("RestrictedApi")
    private val observer = Observer<WorkInfo> {
        viewModelScope.launch {
            nonNotifiedTasks().collect { tasks ->
                val now = Instant.now(Clock.systemDefaultZone()).epochSecond

                tasks.filter { !it.task.notified }
                    .filter { it.task.deadline != null && now - it.task.deadline > 0L}
                    .forEach { task ->
                        val builder = notificationBuilder.setSmallIcon(R.drawable.ic_placeholder_image)
                            .setContentTitle(task.gameTitle)
                            .setContentText(task.task.description)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)

                            // TODO Delete this crap
                        with(NotificationManagerCompat.from(notificationBuilder.mContext)) {
                            notify(task.task.uid, builder.build())
                        }

                        dao.markAsNotified(task.task.uid)
                    }
            }
        }
    }

    private fun nonNotifiedTasks(): Flow<List<TaskWithGameTitle>> {
        return dao.nonNotifiedTasksWithDeadline()
    }

    fun entityById(uid: Int): Flow<Task> = dao.taskById(uid)

    fun insert(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.insert(entity) != 0L) {
                recreateDeadlineWorkRequest()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.delete(uid) != 0) {
                recreateDeadlineWorkRequest()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun setStatus(taskId: Int, status: TaskStatus) {
        viewModelScope.launch {
            dao.setTaskStatus(taskId, status)
            recreateDeadlineWorkRequest()
        }
    }

    fun update(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.update(entity) != 0) {
                recreateDeadlineWorkRequest()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun recreateDeadlineWorkRequest() {
        if (workerId != null) {
            workManager.getWorkInfoByIdLiveData(workerId!!)
                .removeObserver(observer)
        }
        viewModelScope.launch {
            nonNotifiedTasks().collect {
                val pairs = it.map { t -> t.task.uid.toString() to t.task.deadline }
                val request = PeriodicWorkRequestBuilder<DeadlineWorker>(1, TimeUnit.HOURS)
                    .setInputData(workDataOf(*pairs.toTypedArray()))
                    .addTag(WORK_TAG)
                    .build()

                Log.d("viewmodel", "Resetting Worker with tasks: $it")
                workManager.enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, request)

                workerId = request.id
                workManager.getWorkInfoByIdLiveData(workerId!!)
                    .observeForever(observer)
            }
        }
    }

    override fun onCleared() {
        if (workerId != null) {
            workManager.getWorkInfoByIdLiveData(workerId!!)
                .removeObserver(observer)
        }

        super.onCleared()
    }
}

class TaskViewModelFactory(
    private val dao: TaskDao,
    private val workManager: WorkManager,
    private val notificationBuilder: NotificationCompat.Builder
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(dao, workManager, notificationBuilder) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
