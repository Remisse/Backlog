package com.example.backlog.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.model.GameStatus
import com.example.backlog.model.database.entity.Game
import com.example.backlog.viewmodel.validator.RequiredValidator
import java.time.LocalDate

class GameFormState: FormState<Game> {

    var uid: Int = 0
        private set
    val title: FormElement<String> = FormElement("", listOf(RequiredValidator()))
    val platform: FormElement<String> = FormElement("", listOf(RequiredValidator()))
    val genre: FormElement<String> = FormElement("")
    val releaseDate: FormElement<LocalDate?> = FormElement(null)
    val developer: FormElement<String> = FormElement("")
    val publisher: FormElement<String> = FormElement("")
    val status: FormElement<GameStatus> = FormElement(GameStatus.NOT_STARTED, listOf(RequiredValidator()))
    /* TODO Cover */

    var showCalendar by mutableStateOf(false)
    var showCancelDialog by mutableStateOf(false)

    override fun toEntity(): Game {
        if (!validateAll()) throw IllegalStateException("Form has invalid data")
        return Game(
            uid = uid,
            title = title.value,
            platform = platform.value,
            genre = genre.value,
            releaseDate = releaseDate.value?.toEpochDay(),
            developer = developer.value,
            publisher = publisher.value,
            status = status.value,
            coverPath = null
        )
    }

    override fun fromEntity(entity: Game) {
        uid = entity.uid
        title.value = entity.title
        platform.value = entity.platform
        status.value = entity.status
    }

    override fun validateAll(): Boolean {
        return !(title.validate().isNotEmpty()
                || platform.validate().isNotEmpty()
                || status.validate().isNotEmpty()
                || genre.validate().isNotEmpty()
                || releaseDate.validate().isNotEmpty()
                || developer.validate().isNotEmpty()
                || publisher.validate().isNotEmpty())
    }
}
