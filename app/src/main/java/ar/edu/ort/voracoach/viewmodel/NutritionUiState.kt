package ar.edu.ort.voracoach.viewmodel

import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.MealType

/** Hoja abierta sobre 5.3 Add Food. */
enum class AddFoodSheet { NONE, MANUAL, SEARCH }

/**
 * Formulario de carga manual. Los números se guardan como texto para mostrar lo que
 * escribió el usuario (incluso si es inválido) y validar recién al guardar.
 */
data class MealFormState(
    val name: String = "",
    val date: String,
    val mealType: MealType,
    val calories: String = "",
    val protein: String = "",
    val carbs: String = "",
    val fat: String = "",
    val nameError: String? = null,
    val caloriesError: String? = null,
    val proteinError: String? = null,
    val carbsError: String? = null,
    val fatError: String? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null
)

data class FoodSearchState(
    val query: String = "",
    val results: List<Food> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

data class AddFoodUiState(
    val sheet: AddFoodSheet = AddFoodSheet.NONE,
    val form: MealFormState,
    val search: FoodSearchState = FoodSearchState(),
    /** true después de guardar: la navegación vuelve atrás y lo marca como atendido. */
    val isSaved: Boolean = false
)
