package com.example.backlog.viewmodel.validator

interface Validator<in T> {

    val error: String

    fun validate(value: T?): Boolean
}
