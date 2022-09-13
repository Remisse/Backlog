package com.github.backlog.ui.state.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.backlog.model.GameStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.ui.state.form.validator.EmptyIf
import com.github.backlog.ui.state.form.validator.Required
import com.github.backlog.ui.state.form.validator.RequiredIf
import com.github.backlog.utils.localDateFromEpochSecond
import com.github.backlog.utils.toEpochSecond
import java.time.LocalDate

data class GameFormState(
    var uid: Int = 0,
    val steamId:  FormElement<String> = FormElement(""),
    val rawgId:  FormElement<String> = FormElement(""),
    val title: FormElement<String> = FormElement("", Required()),
    val platform: FormElement<String> = FormElement(""),
    val genre: FormElement<String> = FormElement(""),
    val developer: FormElement<String> = FormElement(""),
    val publisher: FormElement<String> = FormElement(""),
    val status: FormElement<GameStatus> = FormElement(GameStatus.NOT_STARTED, Required()),
    val completionDate: FormElement<LocalDate?> = FormElement(
        null,
        RequiredIf { status.value == GameStatus.COMPLETED },
        EmptyIf { status.value != GameStatus.COMPLETED }
    )
) : FormState<Game> {

    var showCancelDialog by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)

    override fun toEntity(): Game {
        if (validateAll().isNotEmpty()) throw IllegalStateException("Form has invalid data")
        return Game(
            uid = uid,
            steamId = steamId.value.takeIf { it != "" },
            rawgId = rawgId.value.takeIf { it != "" },
            title = title.value,
            platform = platform.value.takeIf { it != "" },
            genre = genre.value.takeIf { it != "" },
            developer = developer.value.takeIf { it != "" },
            publisher = publisher.value.takeIf { it != "" },
            status = status.value,
            completionDate = completionDate.value?.toEpochSecond(),
            coverPath = null
        )
    }

    override fun fromEntity(entity: Game) {
        uid = entity.uid
        steamId.value = entity.steamId.orEmpty()
        rawgId.value = entity.rawgId.orEmpty()
        title.value = entity.title
        platform.value = entity.platform.orEmpty()
        genre.value = entity.genre.orEmpty()
        developer.value = entity.developer.orEmpty()
        publisher.value = entity.publisher.orEmpty()
        status.value = entity.status
        completionDate.value = entity.completionDate?.let { localDateFromEpochSecond(it) }
    }
}
