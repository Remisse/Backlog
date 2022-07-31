package com.github.backlog.ui.state.form.validator

interface Validator<in T> {

    val error: String

    fun validate(value: T?): Boolean
}
