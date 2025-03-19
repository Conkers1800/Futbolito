package com.conkers.futbolito.Componentes

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionsScreen(
    onBackClicked: () -> Unit,
    rectWidth: MutableState<Float>,
    rectHeight: MutableState<Float>,
    rectOffsetX: MutableState<Float>,
    rectOffsetY: MutableState<Float>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ajustes del rectángulo
        Text(text = "Ajustes del Campo")

        Text(text = "Ancho: ${rectWidth.value.toInt()}")
        Slider(
            value = rectWidth.value,
            onValueChange = { rectWidth.value = it },
            valueRange = 100f..1080f // Rango para el ancho
        )

        Text(text = "Altura: ${rectHeight.value.toInt()}")
        Slider(
            value = rectHeight.value,
            onValueChange = { rectHeight.value = it },
            valueRange = 100f..1920f // Rango para la altura
        )

        Text(text = "Posición X: ${rectOffsetX.value.toInt()}")
        Slider(
            value = rectOffsetX.value,
            onValueChange = { rectOffsetX.value = it },
            valueRange = 0f..1080f // Rango para posición X
        )

        Text(text = "Posición Y: ${rectOffsetY.value.toInt()}")
        Slider(
            value = rectOffsetY.value,
            onValueChange = { rectOffsetY.value = it },
            valueRange = 0f..1920f // Rango para posición Y
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al menú
        Button(onClick = { onBackClicked() }) {
            Text(text = "Volver al Menú")
        }
    }
}
