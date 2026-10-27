package ar.edu.ort.voracoach.viewmodel

import ar.edu.ort.voracoach.model.AnalyzedFood
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.model.NutritionAnalysis

/** Estado del análisis simulado de 5.5 (voz) y 5.6 (foto). */
sealed interface AnalysisUiState {
    data object Idle : AnalysisUiState

    /** [completedSteps] cuántos pasos de 5.5 ya se marcaron (0 a 3). */
    data class Running(val completedSteps: Int) : AnalysisUiState

    data class Ready(val analysis: NutritionAnalysis) : AnalysisUiState

    data class Error(val message: String) : AnalysisUiState
}

/** Un alimento de 5.7 con los gramos que eligió el usuario. */
data class ReviewItem(val food: AnalyzedFood, val grams: Int) {
    private val factor: Double get() = grams / 100.0
    val calories: Double get() = food.per100g.kcal * factor
    val protein: Double get() = food.per100g.proteinG * factor
    val carbs: Double get() = food.per100g.carbsG * factor
    val fat: Double get() = food.per100g.fatG * factor
}

data class MealReviewUiState(
    val items: List<ReviewItem> = emptyList(),
    val mealType: MealType,
    /** false si se llegó a 5.7 sin un análisis (por ejemplo, tras cerrarse la app). */
    val hasAnalysis: Boolean = false,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    /** true después de guardar: la navegación vuelve al dashboard y lo marca como atendido. */
    val isSaved: Boolean = false
) {
    val calories: Double get() = items.sumOf { it.calories }
    val protein: Double get() = items.sumOf { it.protein }
    val carbs: Double get() = items.sumOf { it.carbs }
    val fat: Double get() = items.sumOf { it.fat }
}
