package ar.edu.ort.voracoach.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Fechas de las comidas en formato yyyy-MM-dd.
 * Usa SimpleDateFormat y no java.time porque java.time no existe antes de Android 8
 * y la app soporta desde Android 7 (minSdk 24).
 */
object MealDates {

    private const val PATTERN = "yyyy-MM-dd"

    fun today(): String = SimpleDateFormat(PATTERN, Locale.US).format(Calendar.getInstance().time)

    /** El DatePicker de Material 3 trabaja con la medianoche UTC del día elegido. */
    fun fromUtcMillis(millis: Long): String =
        SimpleDateFormat(PATTERN, Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(millis)

    fun toUtcMillis(date: String): Long? = try {
        SimpleDateFormat(PATTERN, Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
            .parse(date)?.time
    } catch (e: ParseException) {
        null
    }

    /** "Today", "Yesterday" o la fecha corta en inglés, por ejemplo "Tue, Oct 21". */
    fun label(date: String): String {
        if (date == today()) return "Today"
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        if (date == SimpleDateFormat(PATTERN, Locale.US).format(yesterday.time)) return "Yesterday"
        val millis = toUtcMillis(date) ?: return date
        return SimpleDateFormat("EEE, MMM d", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(millis)
    }

    /** Encabezado de 5.1 como en el Figma: "TUESDAY, 8 SEPTEMBER". */
    fun headerLabel(date: String): String {
        val millis = toUtcMillis(date) ?: return date
        return SimpleDateFormat("EEEE, d MMMM", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(millis)
            .uppercase(Locale.US)
    }

    /** Tipo de comida que corresponde a la hora actual, para precargar el formulario. */
    fun suggestedMealType(): MealType = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..10 -> MealType.BREAKFAST
        in 11..15 -> MealType.LUNCH
        in 16..18 -> MealType.SNACK
        else -> MealType.DINNER
    }
}
