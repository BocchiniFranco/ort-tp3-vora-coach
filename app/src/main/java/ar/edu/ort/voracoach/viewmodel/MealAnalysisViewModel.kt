package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.repository.NutritionDataException
import ar.edu.ort.voracoach.data.repository.NutritionRepository
import ar.edu.ort.voracoach.model.AnalysisPreset
import ar.edu.ort.voracoach.model.AnalysisSource
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.model.NutritionAnalysis
import ar.edu.ort.voracoach.model.NutritionDemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Análisis simulado de comidas: 5.5 (voz), 5.6 (foto) y 5.7 (revisión). El servidor solo
 * estima; nada se guarda en Room hasta que el usuario confirma en 5.7.
 */
@HiltViewModel
class MealAnalysisViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    private val _analysisState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val analysisState: StateFlow<AnalysisUiState> = _analysisState.asStateFlow()

    private var analysisJob: Job? = null

    /** 5.5: analiza lo dictado en 5.4 mientras se van marcando los pasos en pantalla. */
    fun startVoiceAnalysis() {
        if (_analysisState.value != AnalysisUiState.Idle) return
        runAnalysis(showSteps = true) {
            repository.analyzeMeal(AnalysisSource.TEXT, NutritionDemoData.CAPTURED_INPUT, AnalysisPreset.LUNCH)
        }
    }

    /** 5.6: el servidor usa una foto de ejemplo; no se usa la cámara. */
    fun startPhotoAnalysis(force: Boolean = false) {
        if (!force && _analysisState.value != AnalysisUiState.Idle) return
        runAnalysis(showSteps = false) {
            repository.analyzeMeal(AnalysisSource.PHOTO, preset = AnalysisPreset.LUNCH)
        }
    }

    fun retryAnalysis(voice: Boolean) {
        _analysisState.value = AnalysisUiState.Idle
        if (voice) startVoiceAnalysis() else startPhotoAnalysis()
    }

    fun cancelAnalysis() {
        analysisJob?.cancel()
        _analysisState.value = AnalysisUiState.Idle
    }

    /** Deja el análisis listo para 5.7. */
    fun sendToReview(analysis: NutritionAnalysis) = repository.setPendingAnalysis(analysis)

    /** 5.5 ya navegó a 5.7: si se vuelve atrás no tiene que volver a saltar. */
    fun onReviewOpened() {
        _analysisState.value = AnalysisUiState.Idle
    }

    private fun runAnalysis(showSteps: Boolean, request: suspend () -> Result<NutritionAnalysis>) {
        analysisJob?.cancel()
        analysisJob = viewModelScope.launch {
            _analysisState.value = AnalysisUiState.Running(completedSteps = 0)
            val result = async { request() }
            if (showSteps) {
                // Los pasos avanzan aunque el servidor responda antes, para que se puedan leer
                for (step in 1..STEP_COUNT) {
                    delay(STEP_DELAY_MS)
                    _analysisState.value = AnalysisUiState.Running(completedSteps = step)
                }
            }
            _analysisState.value = result.await().fold(
                onSuccess = { analysis ->
                    if (analysis.items.isEmpty()) {
                        AnalysisUiState.Error("VORA couldn't recognise any food. Please try again.")
                    } else {
                        AnalysisUiState.Ready(analysis)
                    }
                },
                onFailure = { error ->
                    AnalysisUiState.Error(
                        (error as? NutritionDataException)?.message
                            ?: "We couldn't analyse this meal. Check your connection and try again."
                    )
                }
            )
        }
    }

    // ------------------------------------ Revisión (5.7) ------------------------------------

    private val _reviewState = MutableStateFlow(reviewStateFrom(repository.pendingAnalysis.value))
    val reviewState: StateFlow<MealReviewUiState> = _reviewState.asStateFlow()

    fun increaseGrams(index: Int) = updateGrams(index, +GRAMS_STEP)

    fun decreaseGrams(index: Int) = updateGrams(index, -GRAMS_STEP)

    fun removeItem(index: Int) = _reviewState.update { state ->
        state.copy(items = state.items.filterIndexed { i, _ -> i != index })
    }

    /** Guarda un registro por alimento, con los macros de los gramos elegidos. */
    fun saveMeal() {
        val state = _reviewState.value
        if (state.isSaving || state.items.isEmpty()) return
        _reviewState.update { it.copy(isSaving = true, saveError = null) }
        viewModelScope.launch {
            val today = MealDates.today()
            val failed = state.items.map { item ->
                repository.addMeal(
                    Meal(
                        name = item.food.name,
                        date = today,
                        mealType = state.mealType,
                        calories = item.calories,
                        protein = item.protein,
                        carbs = item.carbs,
                        fat = item.fat
                    )
                )
            }.any { it.isFailure }
            if (failed) {
                _reviewState.update {
                    it.copy(isSaving = false, saveError = "We couldn't save this meal. Please try again.")
                }
            } else {
                repository.setPendingAnalysis(null)
                _reviewState.update { it.copy(isSaving = false, isSaved = true) }
            }
        }
    }

    fun onSaveHandled() = _reviewState.update { it.copy(isSaved = false) }

    /** "Save without logging": descarta la estimación sin tocar Room. */
    fun discardReview() = repository.setPendingAnalysis(null)

    private fun updateGrams(index: Int, delta: Int) = _reviewState.update { state ->
        state.copy(
            items = state.items.mapIndexed { i, item ->
                if (i == index) item.copy(grams = (item.grams + delta).coerceIn(GRAMS_STEP, MAX_GRAMS)) else item
            }
        )
    }

    private fun reviewStateFrom(analysis: NutritionAnalysis?): MealReviewUiState =
        if (analysis == null) {
            MealReviewUiState(mealType = MealDates.suggestedMealType())
        } else {
            MealReviewUiState(
                items = analysis.items.map { ReviewItem(it, it.grams.roundToInt()) },
                mealType = analysis.mealType,
                hasAnalysis = true
            )
        }

    private companion object {
        const val STEP_COUNT = 3
        const val STEP_DELAY_MS = 900L
        const val GRAMS_STEP = 10
        const val MAX_GRAMS = 2000
    }
}
