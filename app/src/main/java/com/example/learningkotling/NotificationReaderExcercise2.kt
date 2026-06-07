package com.example.learningkotling

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat

@Composable
fun PantallaNotificacion(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    // LocalContext nos da acceso a los recursos del sistema desde Compose
    val context = LocalContext.current
    var permisoConcedido by remember { mutableStateOf(false) }

    // Launcher para solicitar el permiso de notificación (Día 11)
    val launcherPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> permisoConcedido = isGranted }
    )

    Column(
        modifier = modifier
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
                text = "Ejercicio PantallaPermisos 2",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Prueba de Notificaciones Locales",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                // Verificamos si necesitamos pedir el permiso (Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permisoConcedido) {
                    launcherPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Si ya tenemos el permiso (o es un Android antiguo), disparamos la notificación
                    lanzarNotificacionLocal(context)
                }
            }
        ) {
            Text("Enviar Notificación")
        }
    }
}

// --- FUNCIÓN QUE CONSTRUYE Y ENVÍA LA NOTIFICACIÓN ---
fun lanzarNotificacionLocal(context: Context) {
    val canalId = "canal_principal_v1"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 1. Crear el Canal de Notificaciones (Obligatorio en Android 8+)
    val canal = NotificationChannel(
        canalId,
        "Canal Principal",
        NotificationManager.IMPORTANCE_DEFAULT // Importancia estándar (sonido + icono)
    ).apply {
        description = "Canal utilizado para notificaciones del sistema de la app."
    }
    notificationManager.createNotificationChannel(canal)

    // 2. Construir la notificación
    val builder = NotificationCompat.Builder(context, canalId)
        // Usamos un icono por defecto del sistema Android para no complicarnos con recursos gráficos aún
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("¡Hola desde NotificationReader!")
        .setContentText("Esta es tu primera notificación local usando Jetpack Compose.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Hace que la notificación desaparezca automáticamente cuando el usuario la toca
        .setAutoCancel(true)

    // 3. Enviar la notificación (El número 1 es el ID único de esta notificación)
    notificationManager.notify(1, builder.build())
}