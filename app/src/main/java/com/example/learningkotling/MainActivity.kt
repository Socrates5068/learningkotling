package com.example.learningkotling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningkotling.data.local.database.AppDatabase
import com.example.learningkotling.ui.NotificationViewModel
import com.example.learningkotling.ui.NotificationViewModelFactory
import com.example.learningkotling.ui.screens.NotificationHistoryScreen
import com.example.learningkotling.data.local.NotificationRepository
import com.example.learningkotling.ui.theme.LearningKotlingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializamos la base de datos y el DAO
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { NotificationRepository(database.notificationDao()) }
        val viewModel: NotificationViewModel by viewModels {
            NotificationViewModelFactory(repository)
        }

        setContent {
            LearningKotlingTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: NotificationViewModel) {
    // Estado para controlar qué ejercicio mostrar
    // 0 = Menú, 1 = Ejercicio Estado, etc.
    var currentScreen by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                0 -> MenuPrincipal(onSelectExercise = { id -> currentScreen = id })
                1 -> InteractiveScreen(onBack = { currentScreen = 0 })
                2 -> AppNavigationExcercise(onBack = { currentScreen = 0 })
                3 -> PantallaContador(onBack = { currentScreen = 0 })
                4 -> PantallaCarga(onBack = { currentScreen = 0 })
                5 -> PantallaPermisos(onBack = { currentScreen = 0 })
                6 -> PantallaNotificacion(onBack = { currentScreen = 0 })
                7 -> NotificationHistoryScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = 0 }
                )
                // Aquí irás añadiendo más números para nuevos ejercicios
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("1. Estado en Compose (Carrito de compras)")
        }

        // Navigation Exercise
        Button(
            onClick = { onSelectExercise(2) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("2. Navigation Exercise")
        }

        // Espacio para futuros botones
        Button(
            onClick = { onSelectExercise(3) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("3. Counter Exercise")
        }

        // Coroutines Exercise
        Button(
            onClick = { onSelectExercise(4) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("4. Coroutines Exercise")
        }

        // PantallaPermisos Exercise
        Button(
            onClick = { onSelectExercise(5) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("5. Pantalla Permisos Exercise")
        }

        // PantallaPermisos Exercise 2
        Button(
            onClick = { onSelectExercise(6) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("6. Pantalla Permisos Exercise 2")
        }

        // Room Database Exercise
        Button(
            onClick = { onSelectExercise(7) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text("7. Historial de Notificaciones (Room)")
        }
    }
}
