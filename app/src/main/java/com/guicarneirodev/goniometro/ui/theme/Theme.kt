package com.guicarneirodev.goniometro.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = SecondaryDark,
    tertiary = PrimaryLight,
    background = SecondaryDark,
    surface = SecondaryDark,
    onPrimary = PrimaryLight,
    onSecondary = PrimaryLight,
    onTertiary = SecondaryDark,
    onBackground = PrimaryLight,
    onSurface = PrimaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = PrimaryLight,
    tertiary = SecondaryDark,
    background = SecondaryDark,
    surface = SecondaryDark,
    onPrimary = PrimaryLight,
    onSecondary = SecondaryDark,
    onTertiary = PrimaryLight,
    onBackground = PrimaryLight,
    onSurface = PrimaryLight
)

@Composable
fun GoniometroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SecondaryDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}