package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.remote.MockScenario
import ar.edu.ort.voracoach.data.remote.MockScenarioConfig
import ar.edu.ort.voracoach.data.repository.WorkoutDataException
import ar.edu.ort.voracoach.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel del catálogo de entrenamientos (4.1), RF05 y RF07.
 *
 * @HiltViewModel + @Inject constructor: Hilt lo crea y le pasa el repositorio y la
 * configuración del mock. En el grafo se obtiene con hiltViewModel().
 *
 * El ViewModel sobrevive a la rotación: init{} corre UNA sola vez, así que girar
 * el celular no repite la consulta ni pierde lo cargado (RF07).
 */
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository,
    private val mockScenarioConfig: MockScenarioConfig
) : ViewModel() {

    // Privado y modificable adentro; público y de solo lectura afuera.
    private val _uiState = MutableStateFlow<WorkoutsUiState>(WorkoutsUiState.Loading)
    val uiState: StateFlow<WorkoutsUiState> = _uiState.asStateFlow()

    // Escenario de prueba elegido en el selector (para la defensa, RF05)
    private val _scenario = MutableStateFlow(mockScenarioConfig.scenario)
    val scenario: StateFlow<MockScenario> = _scenario.asStateFlow()

    // Modo lento: pide al mock 3 segundos de demora para ver el estado Loading
    private val _slowMode = MutableStateFlow(mockScenarioConfig.delayMs > 0)
    val slowMode: StateFlow<Boolean> = _slowMode.asStateFlow()

    init {
        loadWorkouts() // "al entrar se carga el catálogo"
    }

    /** Botón Retry del estado de error. */
    fun retry() = loadWorkouts()

    /** Cambia el escenario del mock (normal, empty, error, malformed) y recarga. */
    fun onScenarioSelected(scenario: MockScenario) {
        mockScenarioConfig.scenario = scenario
        _scenario.value = scenario
        loadWorkouts()
    }

    /** Activa o desactiva la demora de 3 s y recarga. */
    fun onSlowModeChanged(enabled: Boolean) {
        mockScenarioConfig.delayMs = if (enabled) SLOW_DELAY_MS else 0
        _slowMode.value = enabled
        loadWorkouts()
    }

    private fun loadWorkouts() {
        _uiState.value = WorkoutsUiState.Loading
        // viewModelScope.launch: corrutina que se cancela sola si el ViewModel se destruye
        viewModelScope.launch {
            _uiState.value = repository.fetchWorkouts().fold(
                onSuccess = { result ->
                    if (result.data.isEmpty()) WorkoutsUiState.Empty
                    else WorkoutsUiState.Success(result.data, isOffline = result.isFromCache)
                },
                onFailure = { error -> WorkoutsUiState.Error(error.toUserMessage()) }
            )
        }
    }

    /** Nuestros errores ya traen un mensaje claro; los demás (red, JSON roto) uno genérico. */
    private fun Throwable.toUserMessage(): String =
        if (this is WorkoutDataException) message.orEmpty()
        else "We couldn't load your workouts. Check your connection and try again."

    private companion object {
        const val SLOW_DELAY_MS = 3_000L
    }
}