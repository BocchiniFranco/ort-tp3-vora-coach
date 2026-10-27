package ar.edu.ort.voracoach.viewmodel

/**
 * Estado del entrenamiento activo (4.5). Es una data class (no sealed) porque la
 * pantalla siempre tiene contenido: es una SIMULACIÓN local, sin carga ni errores.
 *
 * @property currentSet serie que se está haciendo ahora (empieza en 1).
 * @property restSecondsLeft segundos de descanso que faltan (0 = no está descansando).
 * @property isFinished true cuando terminó el último ejercicio: el grafo navega a 4.7.
 */
data class ActiveWorkoutUiState(
    val exerciseNumber: Int,
    val totalExercises: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val currentSet: Int,
    val weightKg: Double,
    val lastSession: String,
    val restSecondsLeft: Int,
    val isFinished: Boolean = false
) {
    /** Propiedad calculada: está descansando si quedan segundos de descanso. */
    val isResting: Boolean get() = restSecondsLeft > 0
}