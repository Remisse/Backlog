package com.example.backlog.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.model.GameStatus
import com.example.backlog.model.database.entity.Game
import com.example.backlog.ui.state.validator.Required
import java.time.LocalDate
import kotlin.reflect.full.declaredMemberProperties

data class GameFormState(
    var uid: Int = 0,
    val title: FormElement<String> = FormElement("", Required()),
    val platform: FormElement<String> = FormElement("", Required()),
    val genre: FormElement<String> = FormElement(""),
    val releaseDate: FormElement<LocalDate?> = FormElement(null),
    val developer: FormElement<String> = FormElement(""),
    val publisher: FormElement<String> = FormElement(""),
    val status: FormElement<GameStatus> = FormElement(GameStatus.NOT_STARTED, Required())
) : FormState<Game> {

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
        genre.value = entity.genre.orEmpty()
        releaseDate.value = entity.releaseDate?.let { LocalDate.ofEpochDay(it) }
        developer.value = entity.developer.orEmpty()
        publisher.value = entity.publisher.orEmpty()
        status.value = entity.status
    }

    // TODO Extract abstract class
    override fun validateAll(): Boolean {
        return this::class.declaredMemberProperties.map { it.getter.call(this) }
            .filterIsInstance<FormElement<*>>()
            .all { it.validate().isEmpty() }
    }
}
