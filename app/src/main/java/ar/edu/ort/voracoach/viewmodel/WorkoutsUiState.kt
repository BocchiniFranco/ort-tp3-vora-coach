package ar.edu.ort.voracoach.viewmodel

import ar.edu.ort.voracoach.model.Workout

/**
 * Estado del catálogo de entrenamientos (4.1), RF05.
 *
 * "sealed interface": la pantalla SOLO puede estar en uno de estos 4 estados. Al usar
 * "when (state)" Kotlin obliga a contemplar todos: si falta uno, no compila.
 */
sealed interface WorkoutsUiState {
    /** Esperando la respuesta del servidor. */
    data object Loading : WorkoutsUiState

    /** Llegaron entrenamientos. isOffline = true si vienen de la caché de Room (RF02). */
    data class Success(val workouts: List<Workout>, val isOffline: Boolean = false) :
        WorkoutsUiState

    /** El servidor respondió bien pero sin entrenamientos (escenario "empty"). */
    data object Empty : WorkoutsUiState

    /** Falló la consulta: se muestra el mensaje y un botón de reintento. */
    data class Error(val message: String) : WorkoutsUiState
}