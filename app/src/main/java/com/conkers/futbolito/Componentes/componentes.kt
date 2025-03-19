package com.conkers.futbolito.Componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.conkers.futbolito.R
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    posX: MutableState<Float>,
    posY: MutableState<Float>,
    velX: MutableState<Float>,
    velY: MutableState<Float>,
    sensitivityFactor: Float,
    zFactor: Float,
    rectWidth: Float,
    rectHeight: Float,
    rectOffsetX: Float,
    rectOffsetY: Float,
    onGoalScored: (Color) -> Unit,
    onBackToMenu: () -> Unit
) {
    val circleRadiusPx = 20f // Tamaño del balón
    var showMenu by remember { mutableStateOf(false) } // Estado para el mini menú

    // Movimiento del balón: Pausa cuando el menú está abierto
    LaunchedEffect(showMenu) {
        while (!showMenu) { // Solo se ejecuta cuando el menú está cerrado
            velX.value *= 0.9f // Amortiguación de velocidad en X
            velY.value *= 0.9f // Amortiguación de velocidad en Y

            posX.value += velX.value
            posY.value += velY.value

            // Detectar colisiones con los bordes del rectángulo
            detectBorderCollisions(
                posX, posY, velX, velY, circleRadiusPx,
                rectOffsetX, rectOffsetY, rectWidth, rectHeight
            )

            delay(16L) // Simular 60 FPS
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo de la cancha
        Image(
            painter = painterResource(id = R.drawable.futbolito),
            contentDescription = "Cancha de futbol",
            modifier = Modifier.fillMaxSize()
        )

        // Dibujar el balón y las colisiones
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White,
                center = Offset(posX.value, posY.value),
                radius = circleRadiusPx
            )

            drawRect(
                color = Color.Red.copy(alpha = 0.2f),
                topLeft = Offset(rectOffsetX, rectOffsetY),
                size = Size(rectWidth, rectHeight)
            )
        }

        // Botón flotante para abrir el mini menú
        Button(
            onClick = { showMenu = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(text = "Menú")
        }

        // Mini menú (mostrado cuando showMenu es true)
        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)), // Fondo translúcido
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pausa",
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )

                    // Botón para reanudar
                    Button(onClick = { showMenu = false }) {
                        Text(text = "Reanudar")
                    }

                    // Botón para regresar al menú principal
                    Button(onClick = { onBackToMenu() }) {
                        Text(text = "Regresar al Menú")
                    }
                }
            }
        }
    }
}

// Detectar colisiones con los bordes del rectángulo
fun detectBorderCollisions(
    posX: MutableState<Float>,
    posY: MutableState<Float>,
    velX: MutableState<Float>,
    velY: MutableState<Float>,
    radius: Float,
    rectOffsetX: Float,
    rectOffsetY: Float,
    rectWidth: Float,
    rectHeight: Float
) {
    if (posX.value - radius < rectOffsetX || posX.value + radius > rectOffsetX + rectWidth) {
        velX.value = -velX.value * 0.8f
        posX.value = posX.value.coerceIn(rectOffsetX + radius, rectOffsetX + rectWidth - radius)
    }
    if (posY.value - radius < rectOffsetY || posY.value + radius > rectOffsetY + rectHeight) {
        velY.value = -velY.value * 0.8f
        posY.value = posY.value.coerceIn(rectOffsetY + radius, rectOffsetY + rectHeight - radius)
    }
}