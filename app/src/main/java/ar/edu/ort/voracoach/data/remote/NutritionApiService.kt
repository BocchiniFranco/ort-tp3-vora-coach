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
 * Endpoints opcionales de nutrición. Las comidas no se mandan al servidor: se guardan en Room.
 * Devuelven Response<...> para que el repositorio pueda distinguir 400, 404 o 503.
 */
interface NutritionApiService {

    /** Los parámetros null no se mandan: sin q trae todos los alimentos. */
    @GET("v1/foods")
    suspend fun searchFoods(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): Response<FoodSearchResponseDto>

    @POST("v1/nutrition/analyses")
    suspend fun analyzeMeal(
        @Body request: NutritionAnalysisRequestDto
    ): Response<ApiResponseDto<NutritionAnalysisDto>>
}
