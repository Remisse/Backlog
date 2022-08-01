package com.github.backlog.util

import android.content.Context
import androidx.annotation.RawRes

// From https://stackoverflow.com/a/47225654
fun Context.readRaw(@RawRes resourceId: Int): String {
    return resources.openRawResource(resourceId).bufferedReader(Charsets.UTF_8).use { it.readText() }
}
