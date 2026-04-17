package com.example.learningkotling

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Importación CLAVE

class CounterExercise : ViewModel() {
    // Movemos el estado de Compose AQUÍ adentro.
    // Ocultamos la variable real (private) y exponemos solo su valor de lectura
    // Esto evita que la UI modifique el estado directamente sin pasar por las funciones.
    var contador by mutableStateOf(0)
        private set // Solo el ViewModel puede cambiar el valor directamente

    // Función pública para que la UI pida un incremento
    fun incrementar() {
        contador++
    }
}

@Composable
fun PantallaContador(
    onBack: () -> Unit,
    // Instanciamos el ViewModel aquí.
    // Si la pantalla gira, Compose recordará esta instancia y no creará una nueva.
    miViewModel: CounterExercise = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Button(onClick = onBack) {
                Text("Volver")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Ejercicio Contador",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prueba de Fuego ViewModel",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Leemos el estado del ViewModel
        Text(
            text = "Valor: ${miViewModel.contador}",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = {
            // Delegamos la acción al ViewModel.
            // La UI no hace matemáticas, solo envía la "intención" del usuario.
            miViewModel.incrementar()
        }) {
            Text("Incrementar")
        }
    }
}