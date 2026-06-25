package com.example.learningkotling.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import com.example.learningkotling.data.local.entities.NotificationEntity
import com.example.learningkotling.ui.NotificationViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

// Función pura para convertir milisegundos a una hora legible (ej. "14:30" o "05/11 14:30")
fun formatTimestamp(timestamp: Long): String {
    // Usamos el formato de 24 horas y día/mes
    val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Composable
fun NotificationHistoryScreen(
    viewModel: NotificationViewModel,
    onBack: () -> Unit
) {
    // Observamos la lista ya filtrada (cambiamos allNotifications por notifications)
    val notificationList by viewModel.notifications.collectAsStateWithLifecycle()
    // Observamos el texto actual del buscador
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
                Text("Volver al Menú")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.insert(
                        NotificationEntity(
                            appName = "Telegram",
                            sender = "Marcos",
                            content = "Reunión movida a las 5 PM.",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add mock")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Añadimos la barra de búsqueda en la parte superior
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newText -> viewModel.updateSearchQuery(newText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text("Buscar mensajes, apps o contactos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            if (notificationList.isEmpty()) {
                Text("No hay resultados para mostrar.")
            } else {
                LazyColumn {
                    items(notificationList) { notification ->
                        // El Card le da elevación, bordes redondeados y un fondo distinguible
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Columna izquierda para los textos (ocupa el espacio restante)
                                Column(modifier = Modifier.weight(1f)) {
                                    // Encabezado: App y Remitente
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${notification.appName} • ${notification.sender}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Mostramos la hora formateada
                                        Text(
                                            text = formatTimestamp(notification.timestamp),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }

                                    // Contenido del mensaje debajo del encabezado
                                    Text(
                                        text = notification.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                // Botón de borrado a la derecha, fuera de la columna de textos
                                IconButton(
                                    onClick = { viewModel.delete(notification) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete notification",
                                        tint = MaterialTheme.colorScheme.error // Le da un color rojo semántico
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}