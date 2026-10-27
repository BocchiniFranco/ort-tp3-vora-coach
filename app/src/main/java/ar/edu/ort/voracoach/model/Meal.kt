package ar.edu.ort.voracoach.model

/** label es el texto que muestra la UI. */
enum class MealType(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    DINNER("Dinner")
}

/** Macros de la porción completa, no cada 100 g. */
data class Meal(
    val id: Long = 0,        // 0 = todavía no guardada: Room asigna el id al insertar
    val name: String,
    val date: String,        // yyyy-MM-dd
    val mealType: MealType,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val createdAt: Long = 0  // lo asigna el repositorio al guardar
)

/** Objetivos diarios del perfil; los porcentajes del dashboard se calculan contra estos valores. */
data class NutritionGoals(
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)

data class DailyTotals(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val mealCount: Int = 0
)
