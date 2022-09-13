package com.github.backlog.ui.state.form.validator

import java.util.function.BooleanSupplier

class RequiredIf<in T>(private val condition: BooleanSupplier) : Validator<T> {
    private val requiredDelegate: Validator<T> = Required()

    override val error: String = requiredDelegate.error

    override fun validate(value: T?): Boolean {
        return !condition.asBoolean || requiredDelegate.validate(value)
    }
}
