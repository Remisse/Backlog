package com.github.backlog.ui.state.form.validator

import java.util.function.BooleanSupplier

class EmptyIf<in T>(private val condition: BooleanSupplier) : Validator<T> {
    override val error: String = "not empty"

    override fun validate(value: T?): Boolean {
        return !condition.asBoolean || when(value) {
            is String -> value == ""
            else -> value == null
       }
    }
}
