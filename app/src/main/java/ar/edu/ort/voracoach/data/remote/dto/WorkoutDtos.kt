package ar.edu.ort.voracoach.data.remote.dto

import ar.edu.ort.voracoach.model.Exercise
import ar.edu.ort.voracoach.model.Workout
import ar.edu.ort.voracoach.model.WorkoutDetail
import com.google.gson.annotations.SerializedName

// =====================================================================================
// DTOs del flujo 4: copia EXACTA del JSON del mock (RF06).
// Todos los campos son nullables (?): si el servidor manda un JSON incompleto,
// la app no se cae. Los mappers toDomain() validan y convierten al modelo.
// =====================================================================================

/** Un elemento de GET /v1/workouts. */
data class WorkoutDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("level") val level: String?,
    @SerializedName("durationMinutes") val durationMinutes: Int?,
    @SerializedName("exerciseCount") val exerciseCount: Int?
)

/** Respuesta de GET /v1/workouts/{workoutId}. */
data class WorkoutDetailDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("level") val level: String?,
    @SerializedName("durationMinutes") val durationMinutes: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("exercises") val exercises: List<ExerciseDto>?
)

/** Un ejercicio dentro del detalle. */
data class ExerciseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("muscleGroup") val muscleGroup: String?,
    @SerializedName("sets") val sets: Int?,
    @SerializedName("reps") val reps: Int?,
    @SerializedName("restSeconds") val restSeconds: Int?,
    @SerializedName("instructions") val instructions: String?,
    @SerializedName("tip") val tip: String?
)

// ------------------------------- Mappers DTO -> modelo -------------------------------

/**
 * Convierte el DTO en modelo. Devuelve null si falta el id o el nombre:
 * sin esos datos no se puede mostrar ni abrir el entrenamiento.
 */
fun WorkoutDto.toDomain(): Workout? {
    if (id == null || name == null) return null
    return Workout(
        id = id,
        name = name,
        level = level.orEmpty(),               // si viene null, queda ""
        durationMinutes = durationMinutes ?: 0, // si viene null, queda 0
        exerciseCount = exerciseCount ?: 0
    )
}

fun ExerciseDto.toDomain(): Exercise? {
    if (id == null || name == null) return null
    return Exercise(
        id = id,
        name = name,
        muscleGroup = muscleGroup.orEmpty(),
        sets = sets ?: 0,
        reps = reps ?: 0,
        restSeconds = restSeconds ?: 0,
        instructions = instructions.orEmpty(),
        tip = tip.orEmpty()
    )
}

fun WorkoutDetailDto.toDomain(): WorkoutDetail? {
    if (id == null || name == null) return null
    return WorkoutDetail(
        id = id,
        name = name,
        level = level.orEmpty(),
        durationMinutes = durationMinutes ?: 0,
        description = description.orEmpty(),
        // mapNotNull: convierte cada ejercicio y DESCARTA los que vienen rotos (null)
        exercises = exercises.orEmpty().mapNotNull { it.toDomain() }
    )
}