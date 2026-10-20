package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.repository.NotificationRepository
import ar.edu.ort.voracoach.model.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Estado de la pantalla 2.2 Notifications.
 * Se declara en el mismo archivo que su ViewModel porque es pequeño y solo él lo usa.
 */
data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList()
) {
    /** Propiedad calculada: true si hay al menos una sin leer (habilita "Mark all as read"). */
    val hasUnread: Boolean get() = notifications.any { it.isUnread }
}

/**
 * ViewModel de 2.2 Notifications. Delega en el repositorio @Singleton para que el
 * badge del Home se actualice cuando se leen notificaciones aquí.
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val uiState: StateFlow<NotificationsUiState> = notificationRepository.notifications
        .map { NotificationsUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NotificationsUiState())

    /** Se llama al tocar una notificación (antes de navegar a su destino). */
    fun onNotificationOpened(id: Int) = notificationRepository.markAsRead(id)

    /** Acción "Mark all as read" de la barra superior. */
    fun markAllAsRead() = notificationRepository.markAllAsRead()
}
