package ar.edu.ort.voracoach.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.edu.ort.voracoach.data.local.dao.MealDao
import ar.edu.ort.voracoach.data.local.entity.MealEntity
import ar.edu.ort.voracoach.data.local.dao.WorkoutDao
import ar.edu.ort.voracoach.data.local.entity.ExerciseEntity
import ar.edu.ort.voracoach.data.local.entity.WorkoutDetailEntity
import ar.edu.ort.voracoach.data.local.entity.WorkoutEntity

/**
 * Base única de la app: las tablas de la caché de entrenamientos se suman a entities.
 *
 * Al cambiar el esquema hay que subir version: como no hay migraciones, Room borra y recrea
 * la base. Si no se sube, la app se cierra al abrirla en un equipo que ya la tenía creada.
 */
@Database(
    entities = [
        MealEntity::class,
        WorkoutEntity::class,
        WorkoutDetailEntity::class,
        ExerciseEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VoraDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    abstract fun workoutDao(): WorkoutDao

    companion object {
        const val NAME = "vora_coach.db"
    }
}
