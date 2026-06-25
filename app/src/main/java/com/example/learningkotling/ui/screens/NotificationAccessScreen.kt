package com.example.learningkotling.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat

@Composable
fun NotificationAccessScreen(
    onPermissionGranted: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Estado local para comprobar si el permiso está activo
    var isAccessGranted by remember { mutableStateOf(false) }

    // Función auxiliar para verificar el estatus del permiso en el sistema
    fun checkPermissionStatus(ctx: Context): Boolean {
        val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(ctx)
        return enabledPackages.contains(ctx.packageName)
    }

    // Comprobamos el estatus cada vez que la pantalla se compone
    isAccessGranted = checkPermissionStatus(context)

    // Si el usuario ya dio el permiso, podemos navegar al historial automáticamente
    if (isAccessGranted) {
        LaunchedEffect(Unit) {
            onPermissionGranted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) {
                Text("Volver")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Ejercicio PantallaPermisos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Se requiere acceso a las notificaciones",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Para poder registrar y guardar tus mensajes de WhatsApp de forma local, necesitas activar el interruptor de esta aplicación en los ajustes del sistema.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                // Creamos el Intent que apunta directamente a la pantalla de Ajustes de Acceso a Notificaciones
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        ) {
            Text("Abrir Ajustes de Android")
        }
    }
}