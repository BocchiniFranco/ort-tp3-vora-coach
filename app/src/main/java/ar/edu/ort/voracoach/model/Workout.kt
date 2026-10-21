package ar.edu.ort.voracoach.model

// =====================================================================================
// Modelos de dominio del flujo 4 (Entrenamiento).
// Son clases de Kotlin puro: no saben nada de Retrofit ni de Room (RF06).
// La UI solo usa estos modelos. Sus campos siguen el contrato de la API del mock.
// =====================================================================================

/** Un ejercicio dentro de un entrenamiento (se muestra en 4.3 Workout Detail). */
data class Exercise(
    val id: String,
    val name: String,          // ej. "Bench Press"
    val muscleGroup: String,   // músculo principal, ej. "Chest"
    val sets: Int,             // cantidad de series
    val reps: Int,             // repeticiones por serie
    val restSeconds: Int,      // descanso entre series, en segundos
    val instructions: String,  // cómo se hace
    val tip: String            // consejo de técnica
)

/** Un entrenamiento del catálogo (cada fila de la lista en 4.1 Workout Dashboard). */
data class Workout(
    val id: String,            // se usa para abrir SU detalle (NAV04)
    val name: String,          // ej. "Upper Body Strength"
    val level: String,         // "beginner", "intermediate" o "advanced"
    val durationMinutes: Int,  // duración en minutos
    val exerciseCount: Int     // cantidad de ejercicios
)

/** El detalle completo de un entrenamiento, con sus ejercicios (4.3). */
data class WorkoutDetail(
    val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int,
    val description: String,       // descripción del entrenamiento
    val exercises: List<Exercise>  // un entrenamiento tiene VARIOS ejercicios
)