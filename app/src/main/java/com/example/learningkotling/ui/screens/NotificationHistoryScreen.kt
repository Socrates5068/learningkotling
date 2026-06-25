package com.example.learningkotling.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.FloatingActionButton
import com.example.learningkotling.data.local.entities.NotificationEntity
import com.example.learningkotling.ui.NotificationViewModel

@Composable
fun NotificationHistoryScreen(
    viewModel: NotificationViewModel,
    onBack: () -> Unit
) {
    // Convertimos el Flow de Room en un State de Compose que reacciona a cambios en la DB
    val notificationList by viewModel.allNotifications.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
                Text("Volver al Menú")
            }
        },
        // Añadimos el botón flotante en la esquina inferior derecha
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Creamos un registro simulado
                    val mockNotification = NotificationEntity(
                        appName = "WhatsApp",
                        sender = "Carlos",
                        content = "¡Hola! Probando la inserción reactiva en Room.",
                        timestamp = System.currentTimeMillis() // Obtiene la hora actual del sistema en milisegundos
                    )
                    // Le ordenamos al ViewModel que lo guarde en la base de datos
                    viewModel.insert(mockNotification)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add mock notification")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            // Ahora usamos 'notificationList' como una lista normal dentro de un LazyColumn
            if (notificationList.isEmpty()) {
                Text("No hay notificaciones guardadas aún.")
            } else {
                LazyColumn {
                    // Recorremos la lista reactiva
                    items(notificationList) { notification ->
                        Text(
                            text = "[${notification.appName}] ${notification.sender}: ${notification.content}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
