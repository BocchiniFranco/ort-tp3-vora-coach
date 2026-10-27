package ar.edu.ort.voracoach.data.repository

import ar.edu.ort.voracoach.data.local.dao.MealDao
import ar.edu.ort.voracoach.data.local.entity.toDomain
import ar.edu.ort.voracoach.data.local.entity.toEntity
import ar.edu.ort.voracoach.data.remote.NutritionApiService
import ar.edu.ort.voracoach.data.remote.dto.NutritionAnalysisRequestDto
import ar.edu.ort.voracoach.data.remote.dto.toDomain
import ar.edu.ort.voracoach.model.AnalysisPreset
import ar.edu.ort.voracoach.model.AnalysisSource
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.FoodPage
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.NutritionAnalysis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Las comidas las carga el usuario y viven solo en Room: nunca se mandan al servidor.
 * La API se usa únicamente para buscar alimentos y para el análisis simulado.
 *
 * Las acciones devuelven Result para que un fallo de red, un error HTTP o un JSON roto
 * no cierren la app: el ViewModel decide qué mostrar.
 */
@Singleton
class NutritionRepository @Inject constructor(
    private val api: NutritionApiService,
    private val mealDao: MealDao
) {

    // ------------------------------ Registro de comidas (Room) ------------------------------

    /** date en formato yyyy-MM-dd. */
    fun observeMeals(date: String): Flow<List<Meal>> =
        mealDao.observeMealsByDate(date).map { meals -> meals.map { it.toDomain() } }

    /** Emite null si la comida no existe o se eliminó. */
    fun observeMeal(id: Long): Flow<Meal?> =
        mealDao.observeMealById(id).map { it?.toDomain() }

    fun observeDailyTotals(date: String): Flow<DailyTotals> =
        mealDao.observeDailyTotals(date).map { it.toDomain() }

    /** No valida los campos: eso lo hace el ViewModel antes de llamar. Devuelve el id nuevo. */
    suspend fun addMeal(meal: Meal): Result<Long> = runCatching {
        mealDao.insert(
            meal.copy(id = 0, name = meal.name.trim(), createdAt = System.currentTimeMillis())
                .toEntity()
        )
    }

    suspend fun deleteMeal(id: Long): Result<Unit> = runCatching {
        mealDao.deleteById(id)
        Unit
    }

    // ------------------------- Búsqueda y análisis (API opcional) -------------------------

    suspend fun searchFoods(
        query: String,
        page: Int = 1,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Result<FoodPage> = runCatching {
        // Una búsqueda en blanco trae todos los alimentos: no se manda el parámetro q
        val response = api.searchFoods(query.trim().ifBlank { null }, page, pageSize)
        if (!response.isSuccessful) {
            throw NutritionDataException(errorMessageFor(response.code()))
        }
        response.body()?.toDomain()
            ?: throw NutritionDataException("The food list came back empty.")
    }

    private val _pendingAnalysis = MutableStateFlow<NutritionAnalysis?>(null)

    /**
     * Último análisis de voz o foto, a la espera de que el usuario lo revise en 5.7.
     * Vive en memoria: no se guarda nada hasta que el usuario lo confirma.
     */
    val pendingAnalysis: StateFlow<NutritionAnalysis?> = _pendingAnalysis.asStateFlow()

    fun setPendingAnalysis(analysis: NutritionAnalysis?) {
        _pendingAnalysis.value = analysis
    }

    /** Con source TEXT hace falta [text]; con PHOTO el servidor usa una foto de ejemplo. */
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

    /** Textos en inglés, como el resto de la UI. */
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

/** Error con un mensaje listo para mostrarle al usuario. */
class NutritionDataException(message: String) : Exception(message)
