package ar.edu.ort.voracoach.model
// =====================================================================================
// Modelos de dominio del análisis simulado de comidas (flujo 5, endpoint opcional).
// El análisis es una ESTIMACIÓN: el usuario la confirma antes de guardarla en Room.
// =====================================================================================

/** Cómo se describió la comida. apiValue es el texto exacto que espera el servidor. */
enum class AnalysisSource(val apiValue: String) {
    TEXT("text"),  // descripción escrita o dictada (requiere texto)
    PHOTO("photo") // foto simulada: el servidor usa una de ejemplo, no se sube nada
}

/** Comida de ejemplo que devuelve el servidor simulado. */
enum class AnalysisPreset(val apiValue: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    VEGETARIAN("vegetarian")
}

/** Un alimento detectado en el análisis, con los gramos estimados. */
data class AnalyzedFood(
    val foodId: String,
    val name: String,
    val grams: Double,
    val per100g: Macros
)

/** Resultado del análisis: alimentos detectados y totales de la comida completa. */
data class NutritionAnalysis(
    val items: List<AnalyzedFood>,
    val totals: Macros,
    val isSimulated: Boolean,         // siempre true en el mock: no hay IA real
    val requiresConfirmation: Boolean // hay que confirmar antes de guardar
)
