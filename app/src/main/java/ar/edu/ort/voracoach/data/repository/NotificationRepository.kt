// Paquete repository: única puerta de acceso a los datos para los ViewModels.
package ar.edu.ort.voracoach.data.repository

// Modelos de dominio y datos de demostración.
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.model.NotificationItem
// MutableStateFlow / StateFlow: flujo observable que siempre tiene un valor actual.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// update { }: modifica el valor del StateFlow de forma atómica (segura entre hilos).
import kotlinx.coroutines.flow.update
// @Inject: le indica a Hilt/Dagger cómo construir esta clase (inyección por constructor).
import javax.inject.Inject
// @Singleton: Hilt crea UNA sola instancia para toda la app (scope SingletonComponent).
import javax.inject.Singleton

/**
 * Repositorio de notificaciones EN MEMORIA (Flujo 2, demostración).
 *
 * ¿Por qué un repositorio si los datos son de demo?
 *  - Home (badge de no leídas) y Notifications (lista) muestran el MISMO estado.
 *    Al ser @Singleton, ambos ViewModels reciben la misma instancia y ven los cambios
 *    al instante (ej. "Mark all as read" apaga el badge del Home).
 *  - Respeta la arquitectura UI -> ViewModel -> Repository aunque no haya red ni Room.
 *
 * No usa Retrofit ni Room: la consigna prohíbe endpoints falsos para módulos de demostración.
 */
@Singleton
class NotificationRepository @Inject constructor() {

    // Estado privado mutable; hacia afuera solo se expone la versión de solo lectura.
    private val _notifications = MutableStateFlow(DemoData.notifications)

    /** Lista observable de notificaciones. */
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    /** Marca una notificación como leída (al tocarla). */
    fun markAsRead(id: Int) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isUnread = false) else it }
        }
    }

    /** Marca todas como leídas (acción "Mark all as read"). */
    fun markAllAsRead() {
        _notifications.update { list -> list.map { it.copy(isUnread = false) } }
    }
}
