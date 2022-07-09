package com.example.backlog.ui.state

import androidx.compose.runtime.*
import com.example.backlog.viewmodel.validator.Validator

class FormElement<T>(startingValue: T,
                     private val validators: List<Validator<T>> = emptyList()) {

    var value: T by mutableStateOf(startingValue)

    private var _errors: MutableState<List<String>> = mutableStateOf(emptyList())

    /**
     * Run `validate()` first to update the error list
     */
    fun hasErrors(): Boolean {
        return _errors.value.isNotEmpty()
    }

    /**
     * Returns a list of errors if the current value has not passed at least one validator check,
     * an empty list otherwise
     */
    fun validate(): List<String> {
        _errors.value = validators.filter { !it.validate(value) }
            .map { it.error }

        return _errors.value.toList()
    }
}
