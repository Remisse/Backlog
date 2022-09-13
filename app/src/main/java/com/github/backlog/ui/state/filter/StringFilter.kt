package com.github.backlog.ui.state.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.function.BiPredicate

class StringFilter<E>(override val nameResId: Int,
                      initialValue: String,
                      override val condition: BiPredicate<E, String>)
: Filter<String, E> {

    override var value: String by mutableStateOf(initialValue)

    override var enabled: Boolean = true
}
