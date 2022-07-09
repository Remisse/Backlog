package com.example.backlog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class LookAndFeel {
    companion object {
        val FieldModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        val FieldShape = RoundedCornerShape(4.dp)

        val TextFieldTitleModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        val TextValueModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        val DialogSurfaceShape = RoundedCornerShape(4.dp)
        val DialogVerticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        val DialogHorizontalAlignment = Alignment.Start

        val ButtonIconSize = 16.dp
    }
}