package ar.edu.ort.voracoach.viewmodel

import ar.edu.ort.voracoach.model.WorkoutDetail

/**
 * Estado del detalle de un entrenamiento (4.3), RF01 y RF05.
 * sealed interface: la pantalla solo puede estar en uno de estos 3 estados.
 */
sealed interface WorkoutDetailUiState {
    /** Esperando la respuesta del servidor. */
    data object Loading : WorkoutDetailUiState

    /** Llegó el detalle. isOffline = true si viene de la caché de Room (RF02). */
    data class Success(val workout: WorkoutDetail, val isOffline: Boolean = false) :
        WorkoutDetailUiState

    /** Falló la consulta (ej. 404 si el id no existe, o sin conexión). */
    data class Error(val message: String) : WorkoutDetailUiState
}