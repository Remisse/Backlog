package com.example.backlog.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

sealed class LookAndFeel {
    companion object {
        val FieldColumnModifier = Modifier.fillMaxWidth()
            .padding(16.dp)
        val FieldColumnVerticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        val FieldColumnHorizontalAlignment = Alignment.CenterHorizontally

        val FieldModifier = Modifier.fillMaxWidth()
        val FieldShape = RoundedCornerShape(4.dp)

        val TextFieldTitleModifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp)
        val TextValueModifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)

        val DialogSurfaceShape = RoundedCornerShape(4.dp)
        val DialogVerticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        val DialogHorizontalAlignment = Alignment.Start

        val ButtonIconSize = 16.dp

        fun dateFormat(locale: Locale): DateTimeFormatter =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(locale)
    }
}
