package com.github.backlog.ui.state.filter

import java.util.function.BiPredicate

interface Filter<V, E> {

    val nameResId: Int

    var value: V

    var enabled: Boolean

    val condition: BiPredicate<E, V>

    fun test(entity: E): Boolean {
        return !enabled || (enabled && condition.test(entity, value))
    }
}
