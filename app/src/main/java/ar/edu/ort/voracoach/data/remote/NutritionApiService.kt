package ar.edu.ort.voracoach.data.remote

import ar.edu.ort.voracoach.data.remote.dto.ApiResponseDto
import ar.edu.ort.voracoach.data.remote.dto.FoodSearchResponseDto
import ar.edu.ort.voracoach.data.remote.dto.NutritionAnalysisDto
import ar.edu.ort.voracoach.data.remote.dto.NutritionAnalysisRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Endpoints de nutrición del mock (opcionales: búsqueda de alimentos y análisis simulado).
 * Las comidas NO se guardan en el servidor: el registro se hace en Room (RF03, RF04).
 *
 * Igual que WorkoutApiService, devuelven Response<...> para poder revisar el código HTTP.
 */
interface NutritionApiService {

    /**
     * Búsqueda: GET {BASE_URL}v1/foods?q=&page=&pageSize= -> {"data": [...], "meta": {...}}
     * @Query agrega cada parámetro a la URL; si vale null, Retrofit no lo manda.
     */
    @GET("v1/foods")
    suspend fun searchFoods(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): Response<FoodSearchResponseDto>

    /**
     * Análisis simulado: POST {BASE_URL}v1/nutrition/analyses -> {"data": { ... }}
     * @Body convierte el objeto a JSON con Gson y lo manda como cuerpo del pedido.
     */
    @POST("v1/nutrition/analyses")
    suspend fun analyzeMeal(
        @Body request: NutritionAnalysisRequestDto
    ): Response<ApiResponseDto<NutritionAnalysisDto>>
}
