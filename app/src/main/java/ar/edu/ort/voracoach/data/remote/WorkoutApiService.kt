package ar.edu.ort.voracoach.data.remote

import ar.edu.ort.voracoach.data.remote.dto.ApiResponseDto
import ar.edu.ort.voracoach.data.remote.dto.WorkoutDetailDto
import ar.edu.ort.voracoach.data.remote.dto.WorkoutDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Endpoints de entrenamientos del mock (RF01).
 *
 * Es una "interface": solo DESCRIBE los pedidos. Retrofit genera la implementación
 * real cuando se hace retrofit.create(WorkoutApiService::class.java) en NetworkModule.
 *
 * Devuelven Response<...> (y no el DTO directo) para poder revisar el código HTTP:
 * response.isSuccessful es true solo entre 200 y 299 (ej. 503 o 404 -> false).
 */
interface WorkoutApiService {

    /** Catálogo: GET {BASE_URL}v1/workouts -> {"data": [ ... ]} */
    @GET("v1/workouts")
    suspend fun getWorkouts(): Response<ApiResponseDto<List<WorkoutDto>>>

    /**
     * Detalle: GET {BASE_URL}v1/workouts/{workoutId} -> {"data": { ... }}
     * @Path reemplaza {workoutId} en la URL por el valor del parámetro.
     */
    @GET("v1/workouts/{workoutId}")
    suspend fun getWorkoutDetail(
        @Path("workoutId") workoutId: String
    ): Response<ApiResponseDto<WorkoutDetailDto>>
}