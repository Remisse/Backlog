package com.example.backlog.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.model.database.TaskStatus
import com.example.backlog.model.database.entity.Task
import com.example.backlog.viewmodel.validator.RequiredValidator
import java.lang.IllegalStateException
import java.time.LocalDate

class TaskFormState : FormState<Task> {

    var uid: Int = 0
        private set
    val description: FormElement<String> = FormElement("", listOf(RequiredValidator()))
    val gameId: FormElement<Int?> = FormElement(null, listOf(RequiredValidator()))
    val deadline: FormElement<LocalDate?> = FormElement(null, listOf(RequiredValidator()))

    var gameAndPlatform by mutableStateOf("")

    var showGameSelection by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)
    var showCancelDialog by mutableStateOf(false)

    override fun toEntity(): Task {
        if (!validateAll()) throw IllegalStateException()
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
        deadline.value = if (entity.deadlineDateEpochDay == null) {
            null
        } else {
            LocalDate.ofEpochDay(entity.deadlineDateEpochDay)
        }
    }

    override fun validateAll(): Boolean {
        return !(description.validate().isNotEmpty()
                || gameId.validate().isNotEmpty()
                || deadline.validate().isNotEmpty())
    }
}
