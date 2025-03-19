package com.conkers.futbolito

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.conkers.futbolito.Componentes.GameScreen
import com.conkers.futbolito.Componentes.MenuScreen
import com.conkers.futbolito.Componentes.OptionsScreen

class MainActivity : ComponentActivity(), SensorEventListener {

    // Variables para el SensorManager y el acelerómetro
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Variables de posición y velocidad del balón
    private var posX = mutableStateOf(0f)
    private var posY = mutableStateOf(0f)
    private var velX = mutableStateOf(0f)
    private var velY = mutableStateOf(0f)

    private val sensitivityFactor = 2f // Sensibilidad ajustada para movimientos suaves

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización del acelerómetro
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        setContent {
            // Control de la pantalla actual
            var currentScreen by remember { mutableStateOf("Menu") }

            // Configuraciones del rectángulo ajustable (desde Opciones)
            val rectWidth = remember { mutableStateOf(1080f) }
            val rectHeight = remember { mutableStateOf(1622f) }
            val rectOffsetX = remember { mutableStateOf(0f) }
            val rectOffsetY = remember { mutableStateOf(300f) }

            // Inicialización de la posición del balón en el centro de la pantalla
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()
            val screenHeight = resources.displayMetrics.heightPixels.toFloat()
            posX.value = screenWidth / 2
            posY.value = screenHeight / 2

            // Navegación entre las pantallas
            when (currentScreen) {
                "Menu" -> MenuScreen(
                    onPlayClicked = { currentScreen = "Game" },
                    onOptionsClicked = { currentScreen = "Options" }
                )
                "Options" -> OptionsScreen(
                    onBackClicked = { currentScreen = "Menu" },
                    rectWidth = rectWidth,
                    rectHeight = rectHeight,
                    rectOffsetX = rectOffsetX,
                    rectOffsetY = rectOffsetY
                )
                "Game" -> GameScreen(
                    posX = posX,
                    posY = posY,
                    velX = velX,
                    velY = velY,
                    sensitivityFactor = sensitivityFactor,
                    zFactor = 0.5f,
                    rectWidth = rectWidth.value,
                    rectHeight = rectHeight.value,
                    rectOffsetX = rectOffsetX.value,
                    rectOffsetY = rectOffsetY.value,
                    onGoalScored = { color -> println("¡Gol anotado en la portería de color: $color!") },
                    onBackToMenu = {
                        // Pausa el juego y regresa al menú principal
                        velX.value = 0f
                        velY.value = 0f
                        currentScreen = "Menu"
                    }
                )
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Actualización de las velocidades del balón según el acelerómetro
            velX.value += it.values[0] * sensitivityFactor
            velY.value += it.values[1] * sensitivityFactor
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}