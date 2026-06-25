package com.example.learningkotling.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.app.Notification
import com.example.learningkotling.data.local.NotificationRepository
import com.example.learningkotling.data.local.database.AppDatabase
import com.example.learningkotling.data.local.entities.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Heredamos de la clase oficial del SDK de Android
class CustomNotificationListenerService : NotificationListenerService() {

    // Creamos un Scope para ejecutar las llamadas a la base de datos en segundo plano
    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    // Declaramos el repositorio (se inicializará en onCreate)
    private lateinit var repository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
        // Inicializamos la base de datos usando el contexto del servicio
        val database = AppDatabase.getDatabase(applicationContext)
        repository = NotificationRepository(database.notificationDao())
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        // 1. Extraemos los metadatos de la notificación
        val notification = sbn.notification
        val extras = notification.extras

        // 2. Extraemos el paquete (Ej: "com.whatsapp", "org.telegram.messenger")
        val packageName = sbn.packageName

        // 3. Extraemos el título (Suele ser el nombre del contacto o grupo)
        val title = extras.getString(Notification.EXTRA_TITLE) ?: "Desconocido"

        // 4. Extraemos el texto (El contenido del mensaje)
        // Usamos getCharSequence porque a veces Android envía texto con formato
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        // Filtramos para no guardar notificaciones vacías o del sistema sin texto
        if (text.isNotBlank()) {

            // Mapeamos el paquete a un nombre legible (opcional, puedes agregar más)
            val appName = when (packageName) {
                "com.whatsapp" -> "WhatsApp"
                "org.telegram.messenger" -> "Telegram"
                "com.google.android.gm" -> "Gmail"
                else -> packageName // Si no es conocido, guarda el nombre del paquete
            }

            // Creamos la entidad
            val newNotification = NotificationEntity(
                appName = appName,
                sender = title,
                content = text,
                timestamp = sbn.postTime // Tiempo exacto en el que llegó
            )

            // Guardamos en la base de datos
            serviceScope.launch {
                repository.insert(newNotification)
                Log.d("NotificationListener", "Guardado: [$appName] $title - $text")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiamos las corrutinas si el servicio se destruye para evitar memory leaks
        job.cancel()
    }
}