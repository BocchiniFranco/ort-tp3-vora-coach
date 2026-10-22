package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.repository.WorkoutDataException
import ar.edu.ort.voracoach.data.repository.WorkoutRepository
import ar.edu.ort.voracoach.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel del detalle de un entrenamiento (4.3), RF01, RF05 y RF07.
 *
 * SavedStateHandle: Hilt se lo pasa con los argumentos de la ruta "workout/detail/{workoutId}".
 * Así cada entrenamiento abre SU propio detalle (NAV04) y el id sobrevive a la rotación.
 */
@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {

    /** Id del entrenamiento que se abrió (ej. "w01"). Si faltara, es un error de navegación. */
    private val workoutId: String = checkNotNull(savedStateHandle[Screen.ARG_WORKOUT_ID]) {
        "WorkoutDetail se abrió sin workoutId"
    }

    private val _uiState = MutableStateFlow<WorkoutDetailUiState>(WorkoutDetailUiState.Loading)
    val uiState: StateFlow<WorkoutDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail() // una sola vez por pantalla: rotar no repite la consulta (RF07)
    }

    /** Botón "Try again" del estado de error. */
    fun retry() = loadDetail()

    private fun loadDetail() {
        _uiState.value = WorkoutDetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.fetchWorkoutDetail(workoutId).fold(
                onSuccess = { workout -> WorkoutDetailUiState.Success(workout) },
                onFailure = { error ->
                    WorkoutDetailUiState.Error(
                        if (error is WorkoutDataException) error.message.orEmpty()
                        else "We couldn't load this workout. Check your connection and try again."
                    )
                }
            )
        }
    }
}