package com.guicarneirodev.goniometro.utils

fun formatAngleValue(value: String): String {
    val cleanedValue = value.replace("°", "")
    return cleanedValue.takeIf { it.isNotEmpty() }?.let { "$it°" } ?: ""
}