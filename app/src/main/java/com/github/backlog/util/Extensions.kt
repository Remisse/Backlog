package com.github.backlog.util

import android.os.Bundle

/*
 The value has to be of type String since NavArgument does not allow nullable Ints.
 */
fun Bundle.getNullablePositiveInt(key: String): Int? {
    return this.getString(key)?.toInt()
}
