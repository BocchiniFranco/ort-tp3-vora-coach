package ar.edu.ort.voracoach.model
// =====================================================================================
// Modelos de dominio de la búsqueda de alimentos (flujo 5, endpoint opcional).
// Son clases de Kotlin puro: no saben nada de Retrofit ni de Room.
// =====================================================================================

/** Calorías y macronutrientes de una porción (proteínas, carbohidratos y grasas en gramos). */
data class Macros(
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double
)

/** Un alimento del catálogo del servidor. Los macros son cada 100 g. */
data class Food(
    val id: String,
    val name: String,
    val per100g: Macros
)

/** Una página de resultados de la búsqueda de alimentos. */
data class FoodPage(
    val foods: List<Food>,
    val page: Int,
    val pageSize: Int,
    val total: Int // cantidad total de resultados, sumando todas las páginas
) {
    /** true si quedan resultados en páginas siguientes. */
    val hasMore: Boolean get() = page * pageSize < total
}
