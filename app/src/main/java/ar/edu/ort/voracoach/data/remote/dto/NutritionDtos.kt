package ar.edu.ort.voracoach.data.remote.dto

import ar.edu.ort.voracoach.model.AnalyzedFood
import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.FoodPage
import ar.edu.ort.voracoach.model.Macros
import ar.edu.ort.voracoach.model.NutritionAnalysis
import com.google.gson.annotations.SerializedName

// =====================================================================================
// DTOs del flujo 5 (Nutrición): copia EXACTA del JSON del mock (RF06).
// Endpoints opcionales del contrato: búsqueda de alimentos y análisis simulado.
// Todos los campos son nullables (?): si el servidor manda un JSON incompleto,
// la app no se cae.
// =====================================================================================

/** Macros de una porción: {"kcal": 389, "proteinG": 16.9, "carbsG": 66.3, "fatG": 6.9}. */
data class NutritionDto(
    @SerializedName("kcal") val kcal: Double?,
    @SerializedName("proteinG") val proteinG: Double?,
    @SerializedName("carbsG") val carbsG: Double?,
    @SerializedName("fatG") val fatG: Double?
)

/** Un alimento de GET /v1/foods. Los macros son cada 100 g. */
data class FoodDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nutritionPer100g") val nutritionPer100g: NutritionDto?
)

/**
 * Respuesta de GET /v1/foods: además de "data" trae "meta" con la paginación,
 * por eso no alcanza con ApiResponseDto.
 */
data class FoodSearchResponseDto(
    @SerializedName("data") val data: List<FoodDto>?,
    @SerializedName("meta") val meta: PageMetaDto?
)

/** Paginación: página actual, tamaño de página y total de resultados. */
data class PageMetaDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("pageSize") val pageSize: Int?,
    @SerializedName("total") val total: Int?
)

/**
 * Cuerpo de POST /v1/nutrition/analyses.
 * source: "text" (requiere text) o "photo" (el mock usa una foto de ejemplo, no se sube nada).
 * preset: "breakfast", "lunch" o "vegetarian"; si es null el mock usa "breakfast".
 */
data class NutritionAnalysisRequestDto(
    @SerializedName("source") val source: String,
    @SerializedName("text") val text: String? = null,
    @SerializedName("preset") val preset: String? = null
)

/** Respuesta de POST /v1/nutrition/analyses: estimación simulada que se confirma antes de guardar. */
data class NutritionAnalysisDto(
    @SerializedName("source") val source: String?,
    @SerializedName("preset") val preset: String?,
    @SerializedName("isSimulated") val isSimulated: Boolean?,
    @SerializedName("requiresConfirmation") val requiresConfirmation: Boolean?,
    @SerializedName("items") val items: List<AnalysisItemDto>?,
    @SerializedName("totals") val totals: NutritionDto?
)

/** Un alimento detectado en el análisis, con los gramos estimados. */
data class AnalysisItemDto(
    @SerializedName("foodId") val foodId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("grams") val grams: Double?,
    @SerializedName("nutritionPer100g") val nutritionPer100g: NutritionDto?
)

// ------------------------------- Mappers DTO -> modelo -------------------------------

/** Si falta algún macro queda en 0: es preferible a descartar el alimento entero. */
fun NutritionDto.toDomain(): Macros = Macros(
    kcal = kcal ?: 0.0,
    proteinG = proteinG ?: 0.0,
    carbsG = carbsG ?: 0.0,
    fatG = fatG ?: 0.0
)

/** Devuelve null si falta el id o el nombre: sin esos datos no se puede mostrar ni elegir. */
fun FoodDto.toDomain(): Food? {
    if (id == null || name == null) return null
    return Food(
        id = id,
        name = name,
        per100g = nutritionPer100g?.toDomain() ?: Macros(0.0, 0.0, 0.0, 0.0)
    )
}

fun FoodSearchResponseDto.toDomain(): FoodPage {
    // mapNotNull: convierte cada alimento y DESCARTA los que vienen rotos (null)
    val foods = data.orEmpty().mapNotNull { it.toDomain() }
    return FoodPage(
        foods = foods,
        page = meta?.page ?: 1,
        pageSize = meta?.pageSize ?: foods.size,
        total = meta?.total ?: foods.size
    )
}

fun AnalysisItemDto.toDomain(): AnalyzedFood? {
    if (foodId == null || name == null) return null
    return AnalyzedFood(
        foodId = foodId,
        name = name,
        grams = grams ?: 0.0,
        per100g = nutritionPer100g?.toDomain() ?: Macros(0.0, 0.0, 0.0, 0.0)
    )
}

fun NutritionAnalysisDto.toDomain(): NutritionAnalysis = NutritionAnalysis(
    items = items.orEmpty().mapNotNull { it.toDomain() },
    totals = totals?.toDomain() ?: Macros(0.0, 0.0, 0.0, 0.0),
    // Si el servidor no lo aclara, se asume lo más seguro: simulado y a confirmar
    isSimulated = isSimulated ?: true,
    requiresConfirmation = requiresConfirmation ?: true
)
