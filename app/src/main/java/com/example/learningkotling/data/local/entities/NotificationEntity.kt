package com.example.learningkotling.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Definimos el nombre de la tabla en inglés
@Entity(tableName = "notifications_table")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val appName: String,
    val sender: String,
    val content: String,
    val timestamp: Long
)