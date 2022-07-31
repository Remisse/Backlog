package com.github.backlog.viewmodel

import androidx.lifecycle.*
import androidx.work.*
import com.github.backlog.model.DeadlineWorker
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.dao.TaskDao
import com.github.backlog.model.database.entity.Task
import com.github.backlog.model.database.entity.TaskWithGameTitle
import com.github.backlog.ui.state.form.TaskFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val WORK_TAG = "deadline"

class TaskViewModel(private val dao: TaskDao, private val workManager: WorkManager) : ViewModel() {

    private val tasks: Flow<List<Task>> = dao.allTasks()

    val formState = TaskFormState()

    val tasksWithGameTitle: Flow<List<TaskWithGameTitle>> = dao.tasksByGame()

    val dueTasks: Flow<List<Int>> = workManager.getWorkInfosForUniqueWorkLiveData(WORK_TAG)
        .map { list -> list[0].outputData.keyValueMap.keys }
        .map { list -> list.map { k -> Integer.parseInt(k) } }
        .asFlow()

    fun entityById(uid: Int): Flow<Task> = dao.taskById(uid)

    fun insert(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.insert(entity) != 0L) {
            recreateDeadlineWorkRequest()
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.delete(uid) != 0) {
            recreateDeadlineWorkRequest()
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun setStatus(taskId: Int, status: TaskStatus) = viewModelScope.launch {
        dao.setTaskStatus(taskId, status)
        recreateDeadlineWorkRequest()
    }

    fun update(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.update(entity) != 0) {
            recreateDeadlineWorkRequest()
            onSuccess()
        } else {
            onFailure()
        }
    }

    private fun recreateDeadlineWorkRequest() = viewModelScope.launch {
        tasks.collect {
            val pairs = it.map { t -> t.uid.toString() to t.deadlineDateEpochDay }
            val request = PeriodicWorkRequestBuilder<DeadlineWorker>(3, TimeUnit.HOURS)
                .setInputData(workDataOf(*pairs.toTypedArray()))
                .build()

            workManager.enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, request)
        }
    }
}

class TaskViewModelFactory(private val dao: TaskDao, private val workManager: WorkManager)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(dao, workManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
