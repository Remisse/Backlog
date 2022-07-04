package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.database.dao.TaskDao
import com.example.backlog.database.entity.Task
import com.example.backlog.database.entity.TaskWithGameTitle
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    val tasks: LiveData<List<Task>> = dao.getAllTasks().asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        dao.insert(task)
    }

    val tasksWithGameTitle: LiveData<List<TaskWithGameTitle>> = dao.getTasksByGame().asLiveData()
}
