package com.conkers.futbolito.Componentes

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("ContextCastToActivity")
@Composable
fun MenuScreen(
    onPlayClicked: () -> Unit,
    onOptionsClicked: () -> Unit
) {
    val activity = LocalContext.current as? Activity

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Futbolito", fontSize = 50.sp)
            // Botón para jugar
            Button(onClick = { onPlayClicked() }) {
                Text(text = "Jugar")
            }

            // Botón para ir a opciones
            Button(onClick = { onOptionsClicked() }) {
                Text(text = "Opciones")
            }

            // Botón para salir de la aplicación
            Button(onClick = {
                activity?.finish() // Finaliza la actividad actual para cerrar la app
            }) {
                Text(text = "Salir")
            }
        }
    }
}
