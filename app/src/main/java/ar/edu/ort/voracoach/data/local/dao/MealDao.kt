package ar.edu.ort.voracoach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ar.edu.ort.voracoach.data.local.entity.DailyTotalsTuple
import ar.edu.ort.voracoach.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

/** Las consultas que devuelven Flow vuelven a emitir cada vez que cambia la tabla. */
@Dao
interface MealDao {

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY createdAt ASC")
    fun observeMealsByDate(date: String): Flow<List<MealEntity>>

    /** Emite null si la comida no existe o se eliminó. */
    @Query("SELECT * FROM meals WHERE id = :id")
    fun observeMealById(id: Long): Flow<MealEntity?>

    /** COALESCE: en un día sin comidas SUM devuelve NULL y lo convierte en 0. */
    @Query(
        """
        SELECT COALESCE(SUM(calories), 0) AS calories,
               COALESCE(SUM(protein), 0) AS protein,
               COALESCE(SUM(carbs), 0) AS carbs,
               COALESCE(SUM(fat), 0) AS fat,
               COUNT(*) AS mealCount
        FROM meals WHERE date = :date
        """
    )
    fun observeDailyTotals(date: String): Flow<DailyTotalsTuple>

    /** Devuelve el id asignado. */
    @Insert
    suspend fun insert(meal: MealEntity): Long

    /** Devuelve cuántas filas borró: 0 si ya no existía. */
    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
