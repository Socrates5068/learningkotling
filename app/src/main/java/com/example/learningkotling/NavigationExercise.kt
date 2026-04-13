package com.example.learningkotling

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- 1. CONFIGURACIÓN DE NAVEGACIÓN ---
@Composable
fun AppNavigationExcercise(onBack: () -> Unit) {
    // Instanciamos el controlador que recordará el estado de navegación
    val navController = rememberNavController()

    // El NavHost es el "mapa" de nuestras rutas
    NavHost(navController = navController, startDestination = "home") {

        // Ruta 1: Pantalla Principal
        composable(route = "home") {
            HomeScreen(navController, onBack)
        }

        // Ruta 2: Pantalla de Detalle (Recibe un argumento dinámico llamado 'appName')
        composable(
            route = "detail/{appName}",
            arguments = listOf(navArgument("appName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extraemos el argumento de la ruta
            val appName = backStackEntry.arguments?.getString("appName") ?: "Desconocida"
            DetailScreen(navController, appName)
        }
    }
}

// --- 2. PANTALLA PRINCIPAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, onBack: () -> Unit) {
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
                text = "Ejercicio Navegación",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona una aplicación para ver detalles:", modifier = Modifier.padding(bottom = 16.dp))

        // Botones que disparan la navegación pasando el parámetro en la URL/Ruta
        AppListItem("WhatsApp") {
            navController.navigate("detail/WhatsApp")
        }
        AppListItem("Telegram") {
            navController.navigate("detail/Telegram")
        }
        AppListItem("Sistema") {
            navController.navigate("detail/Sistema")
        }
    }
}

// Componente visual reutilizable para la lista
@Composable
fun AppListItem(nombre: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Hacemos la tarjeta clickeable
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = nombre,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- 3. PANTALLA DE DETALLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, appName: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de $appName") },
                navigationIcon = {
                    // Botón para volver atrás
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Historial de $appName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Aquí es donde cargaremos los mensajes de la base de datos de Room en la Semana 3.")
        }
    }
}