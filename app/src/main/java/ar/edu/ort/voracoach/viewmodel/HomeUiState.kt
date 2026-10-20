package ar.edu.ort.voracoach.viewmodel

import ar.edu.ort.voracoach.model.DailySnapshot
import ar.edu.ort.voracoach.model.TodayWorkoutPreview
import ar.edu.ort.voracoach.model.UserProfile

/**
 * Estado del Home (2.1). Es una data class (no sealed) porque el Home siempre tiene
 * contenido: sus datos son locales de demostración y no hay carga ni error posibles.
 *
 * @property unreadNotifications cantidad para el badge de la campana.
 */
data class HomeUiState(
    val user: UserProfile,
    val today: DailySnapshot,
    val todayWorkout: TodayWorkoutPreview,
    val tip: String,
    val greeting: String,
    val unreadNotifications: Int
)
