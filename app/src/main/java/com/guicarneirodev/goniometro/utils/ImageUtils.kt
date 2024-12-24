package com.guicarneirodev.goniometro.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = externalCacheDir ?: throw IllegalStateException("Cache dir não disponível")
    return File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    ).apply {
        Log.d("ImageFile", "Arquivo criado em: $absolutePath")
    }
}