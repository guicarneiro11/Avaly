package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Composable
fun BackgroundImage(currentImageUri: Uri?, modifier: Modifier = Modifier) {
    currentImageUri?.let {
        Box(modifier = modifier) {
            Image(
                painter = rememberAsyncImagePainter(model = it, onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Success -> Log.d(
                            "BackgroundImage",
                            "Imagem carregada com sucesso"
                        )
                        is AsyncImagePainter.State.Error -> Log.e(
                            "BackgroundImage",
                            "Erro ao carregar imagem: ${state.result.throwable}"
                        )
                        else -> {}
                    }
                }),
                contentDescription = "Foto selecionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}