package com.example.backlog.ui.state.validator

interface Validator<in T> {

    val error: String

    fun validate(value: T?): Boolean
}
