package ar.edu.ort.voracoach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ar.edu.ort.voracoach.data.local.entity.ExerciseEntity
import ar.edu.ort.voracoach.data.local.entity.WorkoutDetailEntity
import ar.edu.ort.voracoach.data.local.entity.WorkoutDetailWithExercises
import ar.edu.ort.voracoach.data.local.entity.WorkoutEntity

/**
 * Operaciones de Room sobre la caché de entrenamientos (RF02).
 *
 * Es una "abstract class" (y no interface) para poder escribir las funciones con
 * @Transaction que combinan varias operaciones: o se hacen TODAS o ninguna.
 * Room genera el código SQL de las funciones abstractas al compilar (KSP).
 */
@Dao
abstract class WorkoutDao {

    // ------------------------------- Catálogo (4.1) -------------------------------

    @Query("SELECT * FROM workouts ORDER BY sortOrder ASC")
    abstract suspend fun getWorkouts(): List<WorkoutEntity>

    /** REPLACE: si ya existe una fila con ese id, la pisa con la nueva. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Query("DELETE FROM workouts")
    abstract suspend fun deleteAllWorkouts()

    /** Reemplaza el catálogo guardado por el nuevo, en una sola transacción. */
    @Transaction
    open suspend fun replaceWorkouts(workouts: List<WorkoutEntity>) {
        deleteAllWorkouts()
        insertWorkouts(workouts)
    }

    // ------------------------------- Detalle (4.3) --------------------------------

    /** @Transaction: lee el detalle y sus ejercicios (@Relation) de forma consistente. */
    @Transaction
    @Query("SELECT * FROM workout_details WHERE id = :workoutId")
    abstract suspend fun getWorkoutDetail(workoutId: String): WorkoutDetailWithExercises?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutDetail(detail: WorkoutDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    abstract suspend fun deleteExercisesOf(workoutId: String)

    /** Guarda el detalle y sus ejercicios juntos: si algo falla, no queda nada a medias. */
    @Transaction
    open suspend fun saveWorkoutDetail(
        detail: WorkoutDetailEntity,
        exercises: List<ExerciseEntity>
    ) {
        insertWorkoutDetail(detail)
        deleteExercisesOf(detail.id)
        insertExercises(exercises)
    }
}
