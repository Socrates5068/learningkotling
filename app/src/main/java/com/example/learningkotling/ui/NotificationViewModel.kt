package com.example.learningkotling.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningkotling.data.local.NotificationRepository
import com.example.learningkotling.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// El ViewModel ahora pide el Repositorio en su constructor
class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    // 1. Estado reactivo para el texto del buscador (equivalente a useState(""))
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Combinamos el flujo de la base de datos con el flujo del buscador
    val notifications = repository.allNotifications.combine(_searchQuery) { list, query ->
        if (query.isBlank()) {
            list // Si no hay texto, devolvemos la lista completa
        } else {
            // Filtramos ignorando mayúsculas/minúsculas (ignoreCase)
            list.filter {
                it.appName.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true) ||
                        it.sender.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        // stateIn convierte el Flow resultante en un Estado que la UI puede consumir de forma segura
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Función para que la UI actualice el texto de búsqueda
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insert(notification: NotificationEntity) = viewModelScope.launch {
        repository.insert(notification)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    // La UI llamará a esta función al hacer clic en el ícono de la papelera
    fun delete(notification: NotificationEntity) = viewModelScope.launch {
        repository.delete(notification)
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