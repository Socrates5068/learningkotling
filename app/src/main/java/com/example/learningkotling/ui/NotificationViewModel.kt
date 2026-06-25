package com.example.learningkotling.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningkotling.data.local.NotificationRepository
import com.example.learningkotling.data.local.entities.NotificationEntity
import kotlinx.coroutines.launch

// El ViewModel ahora pide el Repositorio en su constructor
class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    // Exponemos los datos para que la pantalla (NotificationHistoryScreen) los observe
    val allNotifications = repository.allNotifications

    // Las acciones de la UI (como guardar o borrar) pasan por aquí,
    // y usamos viewModelScope para que se ejecuten en segundo plano (Coroutines)
    fun insert(notification: NotificationEntity) = viewModelScope.launch {
        repository.insert(notification)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

// Necesitamos un Factory porque nuestro ViewModel recibe un parámetro (repository)
class NotificationViewModelFactory(private val repository: NotificationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}