package ar.edu.ort.voracoach.model

/** apiValue es el texto exacto que espera el servidor. */
enum class AnalysisSource(val apiValue: String) {
    TEXT("text"),  // requiere el texto de la comida
    PHOTO("photo") // el servidor usa una foto de ejemplo: no se sube nada
}

enum class AnalysisPreset(val apiValue: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    VEGETARIAN("vegetarian")
}

data class AnalyzedFood(
    val foodId: String,
    val name: String,
    val grams: Double,
    val per100g: Macros
)

/** Estimación simulada: el usuario la confirma antes de guardarla como comida. */
data class NutritionAnalysis(
    val items: List<AnalyzedFood>,
    val totals: Macros,
    val isSimulated: Boolean,
    val requiresConfirmation: Boolean,
    val preset: AnalysisPreset? = null
) {
    /** Tipo de comida en el que se guarda; "vegetarian" no dice cuál, se usa el de la hora. */
    val mealType: MealType
        get() = when (preset) {
            AnalysisPreset.BREAKFAST -> MealType.BREAKFAST
            AnalysisPreset.LUNCH -> MealType.LUNCH
            AnalysisPreset.VEGETARIAN, null -> MealDates.suggestedMealType()
        }
}
