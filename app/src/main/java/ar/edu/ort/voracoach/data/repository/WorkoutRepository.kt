package ar.edu.ort.voracoach.data.repository

import ar.edu.ort.voracoach.data.local.dao.WorkoutDao
import ar.edu.ort.voracoach.data.local.entity.exercisesToEntities
import ar.edu.ort.voracoach.data.local.entity.toDomain
import ar.edu.ort.voracoach.data.local.entity.toEntity
import ar.edu.ort.voracoach.data.remote.WorkoutApiService
import ar.edu.ort.voracoach.data.remote.dto.toDomain
import ar.edu.ort.voracoach.model.Workout
import ar.edu.ort.voracoach.model.WorkoutDetail
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio del flujo 4 (RF01, RF02, RF05, RF06): ÚNICA puerta de entrada a los datos
 * de entrenamientos. Coordina las dos fuentes: la API (Retrofit) y la caché (Room).
 *
 * Estrategia "network first con caché":
 *  1. Pide los datos a la API.
 *  2. Si llegan, los GUARDA en Room y los devuelve.
 *  3. Si la API falla (sin red, 503 o JSON roto), devuelve lo guardado en Room.
 *  4. Si tampoco hay nada guardado, devuelve el error.
 * Un fallo NUNCA borra la caché (RF05).
 *
 * @Singleton + @Inject constructor: Hilt le pasa la API y el DAO.
 */
@Singleton
class WorkoutRepository @Inject constructor(
    private val api: WorkoutApiService,
    private val workoutDao: WorkoutDao
) {

    /** Catálogo (4.1). */
    suspend fun fetchWorkouts(): Result<CachedData<List<Workout>>> {
        val remote = runCatching { requestWorkouts() }

        remote.getOrNull()?.let { workouts ->
            // Lista vacía (escenario "empty"): se muestra vacía pero NO se borra la caché
            if (workouts.isNotEmpty()) {
                runCatching {
                    workoutDao.replaceWorkouts(workouts.mapIndexed { index, workout ->
                        workout.toEntity(
                            index
                        )
                    })
                }
            }
            return Result.success(CachedData(workouts, isFromCache = false))
        }

        // Falló la API: se intenta con lo guardado en Room
        val cached =
            runCatching { workoutDao.getWorkouts().map { it.toDomain() } }.getOrDefault(emptyList())
        return if (cached.isNotEmpty()) {
            Result.success(CachedData(cached, isFromCache = true))
        } else {
            Result.failure(remote.exceptionOrNull() ?: WorkoutDataException(GENERIC_ERROR))
        }
    }

    /** Detalle (4.3). */
    suspend fun fetchWorkoutDetail(workoutId: String): Result<CachedData<WorkoutDetail>> {
        val remote = runCatching { requestWorkoutDetail(workoutId) }

        remote.getOrNull()?.let { detail ->
            // Guarda el detalle y sus ejercicios juntos (@Transaction en el DAO)
            runCatching {
                workoutDao.saveWorkoutDetail(
                    detail.toEntity(),
                    detail.exercisesToEntities()
                )
            }
            return Result.success(CachedData(detail, isFromCache = false))
        }

        // Falló la API: ¿ya se había descargado este detalle?
        val cached = runCatching { workoutDao.getWorkoutDetail(workoutId)?.toDomain() }.getOrNull()
        if (cached != null) {
            return Result.success(CachedData(cached, isFromCache = true))
        }

        // Nunca se descargó: si fue un error HTTP (ej. 404) se informa ese; si no, "no disponible offline"
        val error = remote.exceptionOrNull()
        return Result.failure(
            if (error is WorkoutDataException) error else WorkoutDataException(NOT_AVAILABLE_OFFLINE)
        )
    }

    // ------------------------------ Llamadas a la API ------------------------------

    private suspend fun requestWorkouts(): List<Workout> {
        val response = api.getWorkouts()
        if (!response.isSuccessful) throw WorkoutDataException(errorMessageFor(response.code()))
        return response.body()?.data.orEmpty().mapNotNull { it.toDomain() }
    }

    private suspend fun requestWorkoutDetail(workoutId: String): WorkoutDetail {
        val response = api.getWorkoutDetail(workoutId)
        if (!response.isSuccessful) throw WorkoutDataException(errorMessageFor(response.code()))
        return response.body()?.data?.toDomain()
            ?: throw WorkoutDataException("This workout's data is incomplete.")
    }

    /** Mensaje para el usuario según el código HTTP. */
    private fun errorMessageFor(httpCode: Int): String = when (httpCode) {
        HTTP_NOT_FOUND -> "This workout doesn't exist."
        HTTP_SERVICE_UNAVAILABLE -> "The server is unavailable. Please try again."
        else -> "Something went wrong (error $httpCode)."
    }

    private companion object {
        const val HTTP_NOT_FOUND = 404
        const val HTTP_SERVICE_UNAVAILABLE = 503
        const val GENERIC_ERROR = "We couldn't load your workouts."
        const val NOT_AVAILABLE_OFFLINE = "This workout isn't available offline yet."
    }
}

/**
 * Datos + de dónde vinieron. isFromCache = true -> la API falló y se muestran los guardados
 * (la pantalla avisa con un banner "offline").
 */
data class CachedData<T>(val data: T, val isFromCache: Boolean)

/** Error "nuestro" con un mensaje pensado para mostrarle al usuario. */
class WorkoutDataException(message: String) : Exception(message)