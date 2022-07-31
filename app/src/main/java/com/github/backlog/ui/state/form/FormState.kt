package com.github.backlog.ui.state.form

import kotlin.reflect.full.declaredMemberProperties

interface FormState<T> {

    fun toEntity(): T

    fun fromEntity(entity: T)

    fun validateAll(): List<String> {
        return this::class.declaredMemberProperties.map { it.getter.call(this) }
            .filterIsInstance<FormElement<*>>()
            .flatMap { it.validate() }
    }
}
