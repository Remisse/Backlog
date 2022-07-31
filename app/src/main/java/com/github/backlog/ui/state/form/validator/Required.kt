package com.github.backlog.ui.state.form.validator

class Required<in T> : Validator<T> {

    override val error = "required"

    override fun validate(value: T?): Boolean {
        return when (value) {
            is String -> value.isNotEmpty()
            else -> value != null
        }
    }
}
