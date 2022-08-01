package com.github.backlog.ui.state.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.entity.Task
import com.github.backlog.ui.state.form.validator.Required
import java.lang.IllegalStateException
import java.time.LocalDate

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
}
