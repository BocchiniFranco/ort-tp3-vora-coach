package ar.edu.ort.voracoach.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealType

/** Índice en date: el listado y los totales siempre filtran por fecha. */
@Entity(tableName = "meals", indices = [Index("date")])
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: String,      // yyyy-MM-dd como texto: se compara sin problemas de zona horaria
    val mealType: String,  // MealType.name
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val createdAt: Long    // ordena las comidas de un mismo día
)

/** Fila que devuelve la consulta de totales; no es una tabla. */
data class DailyTotalsTuple(
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealCount: Int
)

// ----------------------------- Mappers Entity <-> modelo -----------------------------

fun MealEntity.toDomain(): Meal = Meal(
    id = id,
    name = name,
    date = date,
    // Si el texto no coincide con ningún tipo (no debería pasar) queda como SNACK
    mealType = MealType.entries.firstOrNull { it.name == mealType } ?: MealType.SNACK,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    createdAt = createdAt
)

fun Meal.toEntity(): MealEntity = MealEntity(
    id = id,
    name = name,
    date = date,
    mealType = mealType.name,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    createdAt = createdAt
)

fun DailyTotalsTuple.toDomain(): DailyTotals = DailyTotals(
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    mealCount = mealCount
)
