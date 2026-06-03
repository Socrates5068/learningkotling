package com.example.learningkotling

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaPermisos(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    // 1. Estado para saber qué mostrar en la UI según la decisión del usuario
    var estadoPermiso by remember { mutableStateOf("Permiso no solicitado") }

    // 2. Preparamos el "Launcher". Esto define QUÉ hacer con la respuesta del usuario.
    val launcherPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // isGranted es un Boolean que nos dice si aceptó o denegó
            if (isGranted) {
                estadoPermiso = "¡Permiso concedido! Ya podemos enviar notificaciones."
            } else {
                estadoPermiso = "Permiso denegado. La app funcionará con limitaciones."
            }
        }
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
                text = "Ejercicio PantallaPermisos",
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
            text = estadoPermiso,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = {
            // 3. Al hacer clic, comprobamos la versión de Android.
            // El permiso POST_NOTIFICATIONS solo existe de Android 13 (API 33) en adelante.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Disparamos el cuadro de diálogo del sistema
                launcherPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                estadoPermiso = "Tu versión de Android no requiere pedir este permiso en pantalla."
            }
        }) {
            Text("Solicitar Permiso de Notificaciones")
        }
    }
}