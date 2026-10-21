package ar.edu.ort.voracoach.data.repository

import ar.edu.ort.voracoach.data.remote.WorkoutApiService
import ar.edu.ort.voracoach.data.remote.dto.toDomain
import ar.edu.ort.voracoach.model.Workout
import ar.edu.ort.voracoach.model.WorkoutDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio del flujo 4 (RF01, RF06): ÚNICA puerta de entrada a los datos de
 * entrenamientos. El ViewModel le pide datos y no sabe si vienen de la API o de Room.
 *
 * Por ahora usa solo la API. Cuando exista VoraDatabase se agrega acá la caché en Room
 * (RF02) sin cambiar el ViewModel ni las pantallas.
 *
 * Devuelve Result<T>: Result.success(datos) o Result.failure(error). Así un fallo de red,
 * un error HTTP o un JSON roto NUNCA cierran la app: el ViewModel decide qué mostrar.
 *
 * @Singleton + @Inject constructor: Hilt crea una sola instancia y le pasa la API.
 */
@Singleton
class WorkoutRepository @Inject constructor(
    private val api: WorkoutApiService
) {

    /** Catálogo (4.1): GET /v1/workouts convertido a modelos. */
    suspend fun fetchWorkouts(): Result<List<Workout>> = runCatching {
        val response = api.getWorkouts()
        // isSuccessful = código HTTP entre 200 y 299. Si no (ej. 503), es un error.
        if (!response.isSuccessful) {
            throw WorkoutDataException(errorMessageFor(response.code()))
        }
        // body()?.data: el contenido del sobre {"data": [...]}. Si viene null -> lista vacía.
        response.body()?.data.orEmpty().mapNotNull { it.toDomain() }
    }

    /** Detalle (4.3): GET /v1/workouts/{workoutId} convertido a modelo. */
    suspend fun fetchWorkoutDetail(workoutId: String): Result<WorkoutDetail> = runCatching {
        val response = api.getWorkoutDetail(workoutId)
        if (!response.isSuccessful) {
            throw WorkoutDataException(errorMessageFor(response.code()))
        }
        response.body()?.data?.toDomain()
            ?: throw WorkoutDataException("This workout's data is incomplete.")
    }

    /** Mensaje para el usuario según el código HTTP (textos en inglés, como la UI). */
    private fun errorMessageFor(httpCode: Int): String = when (httpCode) {
        HTTP_NOT_FOUND -> "This workout doesn't exist."
        HTTP_SERVICE_UNAVAILABLE -> "The server is unavailable. Please try again."
        else -> "Something went wrong (error $httpCode)."
    }

    private companion object {
        const val HTTP_NOT_FOUND = 404
        const val HTTP_SERVICE_UNAVAILABLE = 503
    }
}

/** Error "nuestro" con un mensaje pensado para mostrarle al usuario. */
class WorkoutDataException(message: String) : Exception(message)