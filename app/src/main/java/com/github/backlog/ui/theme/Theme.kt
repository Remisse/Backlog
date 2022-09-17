package com.github.backlog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/* Color palettes */
private val DarkColorPalette = darkColorScheme(
    primary = Teal1,
    secondary = LightGreen,
    tertiary = Blue1
)

private val LightColorPalette = lightColorScheme(
    primary = Teal1,
    secondary = Teal200,
    tertiary = Purple700

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BacklogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )

    val systemUiController = rememberSystemUiController()
    val backgroundColor = if (darkTheme) DarkColorPalette.background else LightColorPalette.primary

    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, darkIcons = !darkTheme)
    }
}
