package com.github.backlog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.dao.TaskDao
import com.github.backlog.model.database.backlog.entity.Task
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import com.github.backlog.ui.state.form.TaskFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    val formState = TaskFormState()

    val tasksWithGameTitle: Flow<List<TaskWithGameTitle>> = dao.tasksByGame()

    fun entityById(uid: Int): Flow<Task> = dao.taskById(uid)

    fun insert(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.insert(entity) != 0L) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.delete(uid) != 0) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun setStatus(taskId: Int, status: TaskStatus) {
        viewModelScope.launch {
            dao.setTaskStatus(taskId, status)
        }
    }

    fun update(entity: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            if (dao.update(entity) != 0) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }
}

class TaskViewModelFactory(private val dao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
