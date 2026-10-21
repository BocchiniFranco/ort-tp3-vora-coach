package ar.edu.ort.voracoach.data.repository

import ar.edu.ort.voracoach.data.remote.NutritionApiService
import ar.edu.ort.voracoach.data.remote.dto.NutritionAnalysisRequestDto
import ar.edu.ort.voracoach.data.remote.dto.toDomain
import ar.edu.ort.voracoach.model.AnalysisPreset
import ar.edu.ort.voracoach.model.AnalysisSource
import ar.edu.ort.voracoach.model.FoodPage
import ar.edu.ort.voracoach.model.NutritionAnalysis
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio del flujo 5 (Nutrición): ÚNICA puerta de entrada a los datos de comidas.
 * El ViewModel le pide datos y no sabe si vienen de la API o de Room (RF06).
 *
 * Por ahora usa solo los endpoints opcionales de la API (búsqueda y análisis simulado).
 * Cuando exista VoraDatabase se agrega acá el MealDao para el registro de comidas
 * (RF03, RF04), sin cambiar a quien ya lo use.
 *
 * Devuelve Result<T>: Result.success(datos) o Result.failure(error). Así un fallo de red,
 * un error HTTP o un JSON roto NUNCA cierran la app: el ViewModel decide qué mostrar.
 *
 * @Singleton + @Inject constructor: Hilt crea una sola instancia y le pasa la API.
 */
@Singleton
class NutritionRepository @Inject constructor(
    private val api: NutritionApiService
) {

    /** Búsqueda de alimentos: GET /v1/foods convertido a modelos. */
    suspend fun searchFoods(
        query: String,
        page: Int = 1,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Result<FoodPage> = runCatching {
        // Una búsqueda en blanco trae todos los alimentos: no se manda el parámetro q
        val response = api.searchFoods(query.trim().ifBlank { null }, page, pageSize)
        // isSuccessful = código HTTP entre 200 y 299. Si no (ej. 503), es un error.
        if (!response.isSuccessful) {
            throw NutritionDataException(errorMessageFor(response.code()))
        }
        response.body()?.toDomain()
            ?: throw NutritionDataException("The food list came back empty.")
    }

    /**
     * Análisis simulado: POST /v1/nutrition/analyses convertido a modelo.
     * Con source = TEXT hace falta [text]; con PHOTO el servidor usa una foto de ejemplo.
     */
    suspend fun analyzeMeal(
        source: AnalysisSource,
        text: String? = null,
        preset: AnalysisPreset? = null
    ): Result<NutritionAnalysis> = runCatching {
        val request = NutritionAnalysisRequestDto(
            source = source.apiValue,
            text = text?.trim()?.ifBlank { null },
            preset = preset?.apiValue
        )
        val response = api.analyzeMeal(request)
        if (!response.isSuccessful) {
            throw NutritionDataException(errorMessageFor(response.code()))
        }
        response.body()?.data?.toDomain()
            ?: throw NutritionDataException("This meal analysis is incomplete.")
    }

    /** Mensaje para el usuario según el código HTTP (textos en inglés, como la UI). */
    private fun errorMessageFor(httpCode: Int): String = when (httpCode) {
        HTTP_BAD_REQUEST -> "Please check what you entered and try again."
        HTTP_SERVICE_UNAVAILABLE -> "The server is unavailable. Please try again."
        else -> "Something went wrong (error $httpCode)."
    }

    private companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_SERVICE_UNAVAILABLE = 503
    }
}

/** Error "nuestro" con un mensaje pensado para mostrarle al usuario. */
class NutritionDataException(message: String) : Exception(message)
