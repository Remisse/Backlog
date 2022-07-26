package com.example.backlog.ui.state

interface FormState<T> {

    fun toEntity(): T

    fun fromEntity(entity: T)

    fun validateAll(): List<String>
}
