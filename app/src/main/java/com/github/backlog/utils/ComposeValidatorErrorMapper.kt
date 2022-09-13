package com.github.backlog.utils

import android.content.Context
import com.github.backlog.R

fun errorToLocalizedString(error: String, context: Context): String {
    return when (error) {
        "required" -> context.resources.getString(R.string.form_field_empty_error)
        "not empty" -> context.getString(R.string.form_field_must_be_empty_error)
        else -> context.resources.getString(R.string.form_field_generic_error)
    }
}
