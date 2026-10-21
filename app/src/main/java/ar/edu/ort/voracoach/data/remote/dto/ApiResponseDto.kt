package ar.edu.ort.voracoach.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * "Sobre" de TODA respuesta exitosa del mock: {"data": ...}.
 * <T> es un tipo genérico: el contenido puede ser una lista de entrenamientos
 * (catálogo) o un solo entrenamiento (detalle). Así un mismo sobre sirve para los dos.
 */
data class ApiResponseDto<T>(
    @SerializedName("data") val data: T?
)

/** Respuesta de error del mock: {"error": {"code": "...", "message": "..."}}. */
data class ErrorResponseDto(
    @SerializedName("error") val error: ErrorBodyDto?
)

/** Contenido del error, ej. code = "WORKOUT_NOT_FOUND". */
data class ErrorBodyDto(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?
)