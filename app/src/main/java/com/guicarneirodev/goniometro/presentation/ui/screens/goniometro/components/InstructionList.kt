package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun InstructionList() {
    val instructions = listOf(
        "Dica 1: Escolha entre \"Importar Foto\" ou \"Tirar Foto\".",
        "Dica 2: Lembre-se de escolher um foto que esteja de frente para a articulação a ser medida, para evitar possíveis erros e aumentar a precisão da goniometria",
        "Dica 3: Clique em \"Realizar Goniometria\".",
        "Dica 4: Posicione seu dedo onde seria localizado o \"Eixo\" da articulação e, em seguida, arraste-o em direção à referência do \"Braço fixo do goniômetro\".",
        "Dica 5: Após isso, clique na posição de referência do \"Braço móvel do goniômetro\".",
        "Dica 6: Com isso você terá o ângulo formado entre os dois braços do Goniômetro.",
        "Dica 7: Você pode alterar a referência do ângulo em \"Alterar Quadrante\", dependendo do plano e articulação que estão sendo mensurados.",
        "Dica 8: Caso a goniometria não tenha sido feita corretamente, você pode clicar em \"Reiniciar Goniometria\" para tentar novamente.",
        "Dica 9: Você pode salvar os seus resultados na opção \"Goniometrias\" (Esta ferramenta ainda está em desenvolvimento e haverá melhorias no futuro).",
        "Dica 10: O goniômetro do aplicativo utiliza como referência um goniometro real, tanto em seus braços, eixo e seus quadrantes.",
        "Dica 11: Recomenda-se usar como referência o \"Livro: Fundamentos das Técnicas de Avaliação Musculoesquelética por Marcia E. Epler, M. Lynn Palmer\" em qualquer goniometria realizada, tanto no aplicativo quanto em um goniômetro físico.",
        "Observação: O aplicativo não tem como objetivo substituir o goniômetro físico, apenas servir como uma ferramenta alternativa para os fisioterapeutas. Portanto, ele pode apresentar imprecisões nesta etapa inicial de desenvolvimento, e toda crítica será bem-vinda para melhorar o seu funcionamento."
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color.White.copy(alpha = 0.95f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        items(instructions) { instruction ->
            Text(
                text = instruction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                color = Color(0xFF1E88E5),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Color(0xFF1E88E5).copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun InstructionsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(16.dp),
        title = {
            Text(
                text = "Instruções",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Bold
            )
        },
        text = { InstructionList() },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fechar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}