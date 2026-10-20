package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.repository.NotificationRepository
import ar.edu.ort.voracoach.model.DemoData
import dagger.hilt.android.lifecycle.HiltViewModel
// SharingStarted.WhileSubscribed: mantiene el flujo activo solo mientras la UI lo observa.
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
// map: transforma cada valor emitido; stateIn: convierte un Flow en StateFlow.
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
// LocalTime: hora actual para elegir el saludo (morning / afternoon / evening).
import java.time.LocalTime
import javax.inject.Inject

/**
 * ViewModel del Home (2.1).
 *
 * Recibe por constructor el [NotificationRepository] (inyectado por Hilt) para mostrar
 * en la campana la cantidad de notificaciones no leídas, siempre sincronizada con 2.2.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    notificationRepository: NotificationRepository
) : ViewModel() {

    /**
     * Cada vez que cambian las notificaciones se recalcula el estado del Home.
     * stateIn(...) necesita un valor inicial para que la UI dibuje desde el primer frame.
     */
    val uiState: StateFlow<HomeUiState> = notificationRepository.notifications
        .map { list -> buildState(unread = list.count { it.isUnread }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = buildState(unread = DemoData.notifications.count { it.isUnread })
        )

    private fun buildState(unread: Int) = HomeUiState(
        user = DemoData.alexMorgan,
        today = DemoData.today,
        todayWorkout = DemoData.todayWorkout,
        tip = DemoData.TIP_ON_TRACK,
        greeting = greetingFor(LocalTime.now()),
        unreadNotifications = unread
    )

    /** Saludo según la hora del dispositivo, en inglés como el diseño. */
    private fun greetingFor(time: LocalTime): String = when (time.hour) {
        in 5..11 -> "Good morning"
        in 12..18 -> "Good afternoon"
        else -> "Good evening"
    }
}
