package com.example.backlog.viewmodel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface BacklogViewModel<T> {

    fun insert(entity: T, onSuccess: () -> Unit, onFailure: () -> Unit): Job

    fun delete(uid: Int, onSuccess: () -> Unit, onFailure: () -> Unit): Job

    fun update(entity: T, onSuccess: () -> Unit, onFailure: () -> Unit): Job

    fun entityById(uid: Int): Flow<T>
}
