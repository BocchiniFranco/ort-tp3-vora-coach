package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.model.WorkoutDemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel del entrenamiento activo (4.5). Todo es SIMULADO, como permite la consigna:
 * series, peso y cronómetro de descanso viven en memoria y no se guardan.
 *
 * El cronómetro es una corrutina con delay(1000): baja un segundo por vuelta.
 * Como vive en viewModelScope, se cancela sola si se sale de la pantalla.
 */
@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor() : ViewModel() {

    private val exercises = WorkoutDemoData.activeWorkoutExercises
    private var exerciseIndex = 0

    private val _uiState = MutableStateFlow(stateForExercise(0))
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState.asStateFlow()

    /** Corrutina del cronómetro de descanso (para poder cancelarla). */
    private var restJob: Job? = null

    /** Botón "+": sube el peso. update{} cambia el estado a partir del actual. */
    fun increaseWeight() = _uiState.update { it.copy(weightKg = it.weightKg + WEIGHT_STEP_KG) }

    /** Botón "−": baja el peso, nunca por debajo de 0. */
    fun decreaseWeight() =
        _uiState.update { it.copy(weightKg = (it.weightKg - WEIGHT_STEP_KG).coerceAtLeast(0.0)) }

    /** "Complete Set": pasa a la serie siguiente, al ejercicio siguiente o termina. */
    fun completeSet() {
        val state = _uiState.value
        when {
            // Quedan series de este ejercicio
            state.currentSet < state.sets -> {
                _uiState.update { it.copy(currentSet = it.currentSet + 1) }
                startRest()
            }
            // Quedan ejercicios
            exerciseIndex < exercises.lastIndex -> {
                exerciseIndex++
                _uiState.value = stateForExercise(exerciseIndex)
                startRest()
            }
            // Era la última serie del último ejercicio
            else -> finishWorkout()
        }
    }

    /** "+30s": agrega tiempo al descanso. */
    fun addRestTime() {
        if (_uiState.value.isResting) {
            _uiState.update { it.copy(restSecondsLeft = it.restSecondsLeft + ADD_REST_SECONDS) }
        }
    }

    /** "Skip": corta el descanso. */
    fun skipRest() {
        restJob?.cancel()
        _uiState.update { it.copy(restSecondsLeft = 0) }
    }

    /** "Finish Workout" (o fin natural): avisa al grafo que navegue a 4.7. */
    fun finishWorkout() {
        restJob?.cancel()
        _uiState.update { it.copy(restSecondsLeft = 0, isFinished = true) }
    }

    /** El grafo ya navegó: se apaga la marca para no navegar dos veces. */
    fun onFinishHandled() = _uiState.update { it.copy(isFinished = false) }

    /** Arranca el cronómetro de descanso: baja 1 segundo por vuelta hasta 0. */
    private fun startRest() {
        restJob?.cancel()
        _uiState.update { it.copy(restSecondsLeft = REST_SECONDS) }
        restJob = viewModelScope.launch {
            while (_uiState.value.restSecondsLeft > 0) {
                delay(1_000)
                _uiState.update { it.copy(restSecondsLeft = (it.restSecondsLeft - 1).coerceAtLeast(0)) }
            }
        }
    }

    /** Estado inicial de un ejercicio: serie 1, peso sugerido y sin descanso. */
    private fun stateForExercise(index: Int): ActiveWorkoutUiState {
        val exercise = exercises[index]
        return ActiveWorkoutUiState(
            exerciseNumber = index + 1,
            totalExercises = exercises.size,
            exerciseName = exercise.name,
            sets = exercise.sets,
            reps = exercise.reps,
            currentSet = 1,
            weightKg = exercise.startWeightKg,
            lastSession = exercise.lastSession,
            restSecondsLeft = 0
        )
    }

    private companion object {
        const val REST_SECONDS = 90
        const val ADD_REST_SECONDS = 30
        const val WEIGHT_STEP_KG = 2.5
    }
}