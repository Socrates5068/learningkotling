package com.example.learningkotling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningkotling.ui.theme.LearningKotlingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningKotlingTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // Estado para controlar qué ejercicio mostrar
    // 0 = Menú, 1 = Ejercicio Estado, etc.
    var currentScreen by remember { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                0 -> MenuPrincipal(onSelectExercise = { id -> currentScreen = id })
                1 -> InteractiveScreen(onBack = { currentScreen = 0 })
                // Aquí irás añadiendo más números para nuevos ejercicios
                // 2 -> TuNuevoEjercicio(onBack = { currentScreen = 0 })
            }
        }
    }
}

@Composable
fun MenuPrincipal(onSelectExercise: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mis Ejercicios Kotlin",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón para el primer ejercicio
        Button(
            onClick = { onSelectExercise(1) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("1. Estado en Compose (Carrito de compras)")
        }

        // Espacio para futuros botones
        OutlinedButton(
            onClick = { /* Próximamente */ },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            enabled = false
        ) {
            Text("2. Próximo ejercicio...")
        }
    }
}
