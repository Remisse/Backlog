package com.example.backlog.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.model.TaskStatus
import com.example.backlog.model.database.entity.Task
import com.example.backlog.ui.state.validator.Required
import java.lang.IllegalStateException
import java.time.LocalDate
import kotlin.reflect.full.declaredMemberProperties

data class TaskFormState(
    var uid: Int = 0,
    val description: FormElement<String> = FormElement("", Required()),
    val gameId: FormElement<Int?> = FormElement(null, Required()),
    val deadline: FormElement<LocalDate?> = FormElement(null, Required())
) : FormState<Task> {

    var gameAndPlatform by mutableStateOf("")

    var showGameSelection by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)
    var showCancelDialog by mutableStateOf(false)

    override fun toEntity(): Task {
        if (validateAll().isNotEmpty()) throw IllegalStateException()
        return Task(
            uid = uid,
            description = description.value,
            gameId = gameId.value!!,
            deadlineDateEpochDay = deadline.value!!.toEpochDay(),
            status = TaskStatus.IN_PROGRESS
        )
    }

    override fun fromEntity(entity: Task) {
        uid = entity.uid
        description.value = entity.description
        gameId.value = entity.gameId
        deadline.value = entity.deadlineDateEpochDay?.let { LocalDate.ofEpochDay(it) }
    }

    override fun validateAll(): List<String> {
        return this::class.declaredMemberProperties.map { it.getter.call(this) }
            .filterIsInstance<FormElement<*>>()
            .flatMap { it.validate() }
    }
}
