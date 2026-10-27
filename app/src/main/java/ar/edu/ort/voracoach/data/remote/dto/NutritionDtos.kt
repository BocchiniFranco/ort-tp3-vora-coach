package ar.edu.ort.voracoach.data.remote.dto

import ar.edu.ort.voracoach.model.AnalysisPreset
import ar.edu.ort.voracoach.model.AnalyzedFood
import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.FoodPage
import ar.edu.ort.voracoach.model.Macros
import ar.edu.ort.voracoach.model.NutritionAnalysis
import com.google.gson.annotations.SerializedName

// Campos nullables: un JSON incompleto no rompe el parseo; los mappers completan o descartan.

data class NutritionDto(
    @SerializedName("kcal") val kcal: Double?,
    @SerializedName("proteinG") val proteinG: Double?,
    @SerializedName("carbsG") val carbsG: Double?,
    @SerializedName("fatG") val fatG: Double?
)

data class FoodDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nutritionPer100g") val nutritionPer100g: NutritionDto?
)

/** Además de "data" trae "meta" con la paginación, por eso no alcanza con ApiResponseDto. */
data class FoodSearchResponseDto(
    @SerializedName("data") val data: List<FoodDto>?,
    @SerializedName("meta") val meta: PageMetaDto?
)

data class PageMetaDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("pageSize") val pageSize: Int?,
    @SerializedName("total") val total: Int?
)

/**
 * Con source "text" el servidor exige text (si falta responde 400); con "photo" usa una
 * foto de ejemplo. Sin preset usa "breakfast".
 */
data class NutritionAnalysisRequestDto(
    @SerializedName("source") val source: String,
    @SerializedName("text") val text: String? = null,
    @SerializedName("preset") val preset: String? = null
)

data class NutritionAnalysisDto(
    @SerializedName("source") val source: String?,
    @SerializedName("preset") val preset: String?,
    @SerializedName("isSimulated") val isSimulated: Boolean?,
    @SerializedName("requiresConfirmation") val requiresConfirmation: Boolean?,
    @SerializedName("items") val items: List<AnalysisItemDto>?,
    @SerializedName("totals") val totals: NutritionDto?
)

data class AnalysisItemDto(
    @SerializedName("foodId") val foodId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("grams") val grams: Double?,
    @SerializedName("nutritionPer100g") val nutritionPer100g: NutritionDto?
)

// ------------------------------- Mappers DTO -> modelo -------------------------------

/** Un macro faltante queda en 0: es preferible a descartar el alimento entero. */
fun NutritionDto.toDomain(): Macros = Macros(
    kcal = kcal ?: 0.0,
    proteinG = proteinG ?: 0.0,
    carbsG = carbsG ?: 0.0,
    fatG = fatG ?: 0.0
)

/** null si falta el id o el nombre: sin ellos el alimento no se puede mostrar ni elegir. */
fun FoodDto.toDomain(): Food? {
    if (id == null || name == null) return null
    return Food(
        id = id,
        name = name,
        per100g = nutritionPer100g?.toDomain() ?: Macros(0.0, 0.0, 0.0, 0.0)
    )
}

fun FoodSearchResponseDto.toDomain(): FoodPage {
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
    requiresConfirmation = requiresConfirmation ?: true,
    preset = AnalysisPreset.entries.firstOrNull { it.apiValue == preset }
)
