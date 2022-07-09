package com.example.backlog.viewmodel.validator

class RequiredValidator<in T> : Validator<T> {

    override val error = "required"

    override fun validate(value: T?): Boolean {
        return when (value) {
            is String -> value.isNotEmpty()
            else -> value != null
        }
    }
}
