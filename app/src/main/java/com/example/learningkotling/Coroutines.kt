package com.example.learningkotling

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// --- VIEWMODEL CON COROUTINES ---
class CoroutinesExercise : ViewModel() {

    // Estados para la UI
    var mensajeUI by mutableStateOf("Esperando instrucción...")
        private set

    var estaCargando by mutableStateOf(false)
        private set

    fun cargarDatos() {
        // 1. Iniciamos la corrutina en el Main Thread (seguro para la UI)
        viewModelScope.launch {
            estaCargando = true
            mensajeUI = "Consultando base de datos..."

            // 2. Saltamos al hilo de IO (Background) para no congelar la pantalla
            val resultado = withContext(Dispatchers.IO) {
                // Aquí llamamos a nuestra función suspendida
                simularLecturaPesada()
            }

            // 3. Volvemos automáticamente al Main Thread cuando termina
            mensajeUI = resultado
            estaCargando = false
        }
    }

    // El modificador 'suspend' permite usar delay()
    private suspend fun simularLecturaPesada(): String {
        // delay() pausa la corrutina sin bloquear el hilo
        delay(3000)
        return "✅ ¡1500 notificaciones cargadas!"
    }
}

// --- INTERFAZ DE USUARIO ---
@Composable
fun PantallaCarga(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    coroutinesExercise: CoroutinesExercise = viewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) {
                Text("Volver")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Ejercicio Coroutines",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = coroutinesExercise.mensajeUI, modifier = Modifier.padding(bottom = 16.dp))

        if (coroutinesExercise.estaCargando) {
            // Este spinner seguiría girando gracias a que el hilo principal no está bloqueado
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        } else {
            Button(onClick = { coroutinesExercise.cargarDatos() }) {
                Text("Iniciar lectura de datos")
            }
        }
    }
}