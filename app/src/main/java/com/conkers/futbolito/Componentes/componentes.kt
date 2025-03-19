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

    // Coordenadas de las porterías
    val blueGoalTop = rectOffsetY
    val blueGoalLeft = rectOffsetX + rectWidth / 3
    val blueGoalWidth = rectWidth / 3
    val blueGoalHeight = 50f

    val grayGoalTop = rectOffsetY + rectHeight - 50f
    val grayGoalLeft = rectOffsetX + rectWidth / 3
    val grayGoalWidth = rectWidth / 3
    val grayGoalHeight = 50f

    // Contadores de goles
    var blueScore by remember { mutableStateOf(0) }
    var grayScore by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    // Movimiento del balón
    LaunchedEffect(showMenu) {
        while (!showMenu) {
            velX.value *= 0.9f
            velY.value *= 0.9f

            posX.value += velX.value
            posY.value += velY.value

            detectBorderCollisions(
                posX, posY, velX, velY, circleRadiusPx,
                rectOffsetX, rectOffsetY, rectWidth, rectHeight
            )

            detectGoalCollision(
                posX, posY,
                blueGoalTop, blueGoalLeft, blueGoalWidth, blueGoalHeight,
                grayGoalTop, grayGoalLeft, grayGoalWidth, grayGoalHeight,
                onBlueGoal = {
                    blueScore++
                    resetBallPosition(posX, posY, rectOffsetX + rectWidth / 2, rectOffsetY + rectHeight / 2)
                },
                onGrayGoal = {
                    grayScore++
                    resetBallPosition(posX, posY, rectOffsetX + rectWidth / 2, rectOffsetY + rectHeight / 2)
                }
            )

            delay(16L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.futbolito),
            contentDescription = "Cancha de futbol",
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White,
                center = Offset(posX.value, posY.value),
                radius = circleRadiusPx
            )

            // Dibujar portería azul
            drawRect(
                color = Color.Blue.copy(alpha = 0.7f),
                topLeft = Offset(blueGoalLeft, blueGoalTop),
                size = Size(blueGoalWidth, blueGoalHeight)
            )

            // Dibujar portería gris
            drawRect(
                color = Color.Gray.copy(alpha = 0.7f),
                topLeft = Offset(grayGoalLeft, grayGoalTop),
                size = Size(grayGoalWidth, grayGoalHeight)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Azul: $blueScore", color = Color.Blue)
            Text(text = "Gris: $grayScore", color = Color.Gray)
        }

        Button(
            onClick = { showMenu = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Menú")
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
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

                    Button(onClick = { showMenu = false }) {
                        Text(text = "Reanudar")
                    }

                    Button(onClick = { onBackToMenu() }) {
                        Text(text = "Regresar al Menú")
                    }
                }
            }
        }
    }
}

// Función para detectar colisiones con los bordes
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

// Función para detectar goles en las porterías
fun detectGoalCollision(
    posX: MutableState<Float>,
    posY: MutableState<Float>,
    blueGoalTop: Float,
    blueGoalLeft: Float,
    blueGoalWidth: Float,
    blueGoalHeight: Float,
    grayGoalTop: Float,
    grayGoalLeft: Float,
    grayGoalWidth: Float,
    grayGoalHeight: Float,
    onBlueGoal: () -> Unit,
    onGrayGoal: () -> Unit
) {
    if (posX.value in blueGoalLeft..(blueGoalLeft + blueGoalWidth) &&
        posY.value in blueGoalTop..(blueGoalTop + blueGoalHeight)
    ) {
        onBlueGoal()
    }

    if (posX.value in grayGoalLeft..(grayGoalLeft + grayGoalWidth) &&
        posY.value in grayGoalTop..(grayGoalTop + grayGoalHeight)
    ) {
        onGrayGoal()
    }
}

// Función para resetear la posición del balón
fun resetBallPosition(
    posX: MutableState<Float>,
    posY: MutableState<Float>,
    centerX: Float,
    centerY: Float
) {
    posX.value = centerX
    posY.value = centerY
}
