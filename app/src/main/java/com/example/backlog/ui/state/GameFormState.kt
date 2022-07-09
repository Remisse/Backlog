package com.example.backlog.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.backlog.model.database.GameStatus
import com.example.backlog.model.database.entity.Game
import com.example.backlog.viewmodel.validator.RequiredValidator

class GameFormState: FormState<Game> {

    var uid: Int = 0
        private set
    val title: FormElement<String> = FormElement("", listOf(RequiredValidator()))
    val platform: FormElement<String> = FormElement("", listOf(RequiredValidator()))
    /* TODO Use BigDecimals */
    val retailPrice: FormElement<Float> = FormElement(0.0f)
    val status: FormElement<GameStatus> = FormElement(GameStatus.NOT_STARTED, listOf(RequiredValidator()))
    /* TODO Cover */

    var showCancelDialog by mutableStateOf(false)

    override fun toEntity(): Game {
        if (!validateAll()) throw IllegalStateException("Form has invalid data")
        return Game(
            uid = uid,
            title = title.value,
            platform = platform.value,
            status = status.value,
            retailPrice = retailPrice.value.toLong(),
            coverPath = null
        )
    }

    override fun fromEntity(entity: Game) {
        uid = entity.uid
        title.value = entity.title
        platform.value = entity.platform
        retailPrice.value = entity.retailPrice.toFloat()
        status.value = entity.status
    }

    override fun validateAll(): Boolean {
        return !(title.validate().isNotEmpty()
                || platform.validate().isNotEmpty()
                || retailPrice.validate().isNotEmpty()
                || status.validate().isNotEmpty())
    }
}
