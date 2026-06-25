package com.example.learningkotling.data.local

import com.example.learningkotling.data.local.dao.NotificationDao
import com.example.learningkotling.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {

    // Exponemos el flujo de datos directamente.
    // La UI se suscribirá a esto a través del ViewModel.
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotificationsFlow()

    // Envolvemos la función de inserción
    suspend fun insert(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    // Envolvemos la función de borrado
    suspend fun deleteAll() {
        notificationDao.deleteAllNotifications()
    }
}