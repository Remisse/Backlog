package com.github.backlog.ui.state.form

interface FormState<T> {

    fun toEntity(): T

    fun fromEntity(entity: T)

    fun validateAll(): List<String>
}
