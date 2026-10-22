package ar.edu.ort.voracoach.model

/** Calorías y macronutrientes en gramos. */
data class Macros(
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double
)

data class Food(
    val id: String,
    val name: String,
    val per100g: Macros
)

data class FoodPage(
    val foods: List<Food>,
    val page: Int,
    val pageSize: Int,
    val total: Int // resultados en todas las páginas, no solo en esta
) {
    val hasMore: Boolean get() = page * pageSize < total
}
