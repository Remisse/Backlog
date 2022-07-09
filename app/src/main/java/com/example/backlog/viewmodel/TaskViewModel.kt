package com.example.backlog.viewmodel

import androidx.lifecycle.*
import androidx.work.*
import com.example.backlog.model.DeadlineWorker
import com.example.backlog.model.database.TaskStatus
import com.example.backlog.model.database.dao.TaskDao
import com.example.backlog.model.database.entity.Task
import com.example.backlog.model.database.entity.TaskWithGameTitle
import com.example.backlog.ui.state.TaskFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val WORK_TAG = "deadline"

class TaskViewModel(private val dao: TaskDao, private val workManager: WorkManager) : ViewModel() {

    val formState = TaskFormState()

    private val tasks: Flow<List<Task>> = dao.allTasks()

    val tasksWithGameTitle: Flow<List<TaskWithGameTitle>> = dao.tasksByGame()

    val dueTasks: Flow<List<Int>> = workManager.getWorkInfosForUniqueWorkLiveData(WORK_TAG)
        .map { list -> list[0].outputData.keyValueMap.keys }
        .map { list -> list.map { k -> Integer.parseInt(k) } }
        .asFlow()

    fun taskById(taskId: Int): Flow<Task> = dao.taskById(taskId)

    fun insert(task: Task, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.insert(task) != 0L) {
            recreateDeadlineWorkRequest()
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun delete(taskId: Int, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.delete(taskId) != 0) {
            recreateDeadlineWorkRequest()
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun setTaskStatus(taskId: Int, status: TaskStatus) = viewModelScope.launch {
        dao.setTaskStatus(taskId, status)
        recreateDeadlineWorkRequest()
    }

    fun update(task: Task, onSuccess: () -> Unit, onFailure: () -> Unit) = viewModelScope.launch {
        if (dao.update(task) != 0) {
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
