package com.github.backlog.ui.state.filter

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.function.BiPredicate

class BooleanFilter<E>(@StringRes override val nameResId: Int,
                       initialValue: Boolean,
                       override val condition: BiPredicate<E, Boolean>
) : Filter<Boolean, E> {

    override var value by mutableStateOf(initialValue)

    override var enabled = value
}
