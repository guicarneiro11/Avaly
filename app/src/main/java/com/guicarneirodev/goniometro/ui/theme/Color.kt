package com.guicarneirodev.goniometro.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PrimaryLight = Color(0xFFFEFEFE)
val SecondaryDark = Color(0xFF171D24)
val AccentBlue = Color(0xFF5372FE)

val PrimaryLightAlpha = PrimaryLight.copy(alpha = 0.1f)
val SecondaryDarkAlpha = SecondaryDark.copy(alpha = 0.8f)
val AccentBlueAlpha = AccentBlue.copy(alpha = 0.9f)

val SuccessGreen = Color(0xFF4CAF50)
val ErrorRed = Color(0xFFE53935)

object GoniometroStyle {
    val DefaultCornerRadius = 12.dp
    val LargeCornerRadius = 16.dp
    val SmallCornerRadius = 8.dp

    val DefaultElevation = 4.dp
    val DefaultPadding = 16.dp
    val SmallPadding = 8.dp

    val DefaultIconSize = 24.dp
    val LargeIconSize = 32.dp

    val DefaultAlpha = 0.95f
    val SoftAlpha = 0.6f
    val VerySoftAlpha = 0.1f
}