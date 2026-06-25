package com.example.learningkotling.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learningkotling.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    // Inserta y reemplaza si hay conflicto de IDs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    // Consulta reactiva usando Flow. No requiere 'suspend'
    @Query("SELECT * FROM notifications_table ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    // Función para limpiar la base de datos
    @Query("DELETE FROM notifications_table")
    suspend fun deleteAllNotifications()

    // Borra un registro específico de la tabla
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}