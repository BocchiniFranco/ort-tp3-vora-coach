package ar.edu.ort.voracoach.data.local.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import ar.edu.ort.voracoach.model.Exercise
import ar.edu.ort.voracoach.model.Workout
import ar.edu.ort.voracoach.model.WorkoutDetail

// =====================================================================================
// Tablas de Room para la CACHÉ de entrenamientos (RF02, RF06).
// Entity = una tabla: cada val es una columna y cada objeto es una fila.
// =====================================================================================

/** Tabla del catálogo (4.1). sortOrder guarda el orden en que vino de la API. */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,   // clave primaria: no puede haber dos filas con el mismo id
    val name: String,
    val level: String,
    val durationMinutes: Int,
    val exerciseCount: Int,
    val sortOrder: Int
)

/** Tabla del detalle (4.3), sin los ejercicios (van en su propia tabla). */
@Entity(tableName = "workout_details")
data class WorkoutDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int,
    val description: String
)

/**
 * Tabla de ejercicios. Cada fila apunta a su entrenamiento con workoutId (clave foránea).
 * primaryKeys compuesta: el mismo ejercicio ("e01") puede estar en varios entrenamientos.
 * onDelete = CASCADE: si se borra el detalle, se borran solos sus ejercicios.
 */
@Entity(
    tableName = "workout_exercises",
    primaryKeys = ["workoutId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutDetailEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutId")]
)
data class ExerciseEntity(
    val workoutId: String,
    val exerciseId: String,
    val position: Int,   // orden dentro del entrenamiento (01, 02, 03)
    val name: String,
    val muscleGroup: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val instructions: String,
    val tip: String
)

/**
 * NO es una tabla: es el resultado de leer un detalle JUNTO con sus ejercicios.
 * @Embedded = las columnas del detalle; @Relation = Room busca los ejercicios
 * cuyo workoutId coincide con el id del detalle.
 */
data class WorkoutDetailWithExercises(
    @Embedded val detail: WorkoutDetailEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutId")
    val exercises: List<ExerciseEntity>
)

// ------------------------- Mappers modelo <-> Entity -------------------------

fun Workout.toEntity(sortOrder: Int) = WorkoutEntity(
    id = id,
    name = name,
    level = level,
    durationMinutes = durationMinutes,
    exerciseCount = exerciseCount,
    sortOrder = sortOrder
)

fun WorkoutEntity.toDomain() = Workout(
    id = id,
    name = name,
    level = level,
    durationMinutes = durationMinutes,
    exerciseCount = exerciseCount
)

fun WorkoutDetail.toEntity() = WorkoutDetailEntity(
    id = id,
    name = name,
    level = level,
    durationMinutes = durationMinutes,
    description = description
)

/** Ejercicios del modelo -> filas de la tabla, guardando su posición. */
fun WorkoutDetail.exercisesToEntities(): List<ExerciseEntity> =
    exercises.mapIndexed { index, exercise ->
        ExerciseEntity(
            workoutId = id,
            exerciseId = exercise.id,
            position = index,
            name = exercise.name,
            muscleGroup = exercise.muscleGroup,
            sets = exercise.sets,
            reps = exercise.reps,
            restSeconds = exercise.restSeconds,
            instructions = exercise.instructions,
            tip = exercise.tip
        )
    }

/** Detalle + ejercicios leídos de Room -> modelo de la app (ordenados por posición). */
fun WorkoutDetailWithExercises.toDomain() = WorkoutDetail(
    id = detail.id,
    name = detail.name,
    level = detail.level,
    durationMinutes = detail.durationMinutes,
    description = detail.description,
    exercises = exercises.sortedBy { it.position }.map {
        Exercise(
            id = it.exerciseId,
            name = it.name,
            muscleGroup = it.muscleGroup,
            sets = it.sets,
            reps = it.reps,
            restSeconds = it.restSeconds,
            instructions = it.instructions,
            tip = it.tip
        )
    }
)
