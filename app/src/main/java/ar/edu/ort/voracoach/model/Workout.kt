package ar.edu.ort.voracoach.model
// =====================================================================================
// Modelos de dominio del flujo 4 (Entrenamiento).
// Son clases de Kotlin puro: no saben nada de Retrofit ni de Room.
// La UI solo usa estos modelos.
// =====================================================================================

/** Un ejercicio dentro de un entrenamiento (se muestra en 4.3 Workout Detail). */
data class Exercise(
    val name: String, //nombre del ejercicio
    val sets: Int,     //cantidad series
    val reps: Int,      //repeticiones por serie
    val instructions : String //como se hace
)

/** Un entrenamiento del catálogo (cada fila de la lista en 4.1 Workout Dashboard). */
data class Workout(
    val id: String, //id para abrir detalle
    val name: String, // ej. Full body
    val level: String, // ej Beginner
    val durationMinutes: Int //duracion en minutos
)
/** El detalle completo de un entrenamiento, con sus ejercicios (4.3). */
data class WorkoutDetail(
    val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int,
    val exercises: List<Exercise> //1 entrenamiento tiene varios ejercicios
)

