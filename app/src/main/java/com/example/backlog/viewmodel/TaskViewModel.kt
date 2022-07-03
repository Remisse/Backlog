package com.example.backlog.viewmodel

import androidx.lifecycle.*
import com.example.backlog.database.dao.TaskDao
import com.example.backlog.database.entities.Task
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    val backlog: LiveData<List<Task>> = dao.getAllTasks().asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        dao.insert(task)
    }
}
