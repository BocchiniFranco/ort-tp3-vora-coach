package ar.edu.ort.voracoach.model
// =====================================================================================
// Datos de DEMOSTRACIÓN del flujo 4 (Entrenamiento) - Integrante 2.
// Las pantallas demo (4.2, 4.4 a 4.7) los muestran sin pedir nada al servidor.
// Están separados de DemoData.kt (Integrante 1) para no pisarnos al commitear.
// =====================================================================================

/** Un día del selector semanal de 4.2 (ej. "TUE" 8). */
data class PlanDay(
    val dayLabel: String,   // abreviatura del día, como en Figma
    val dateNumber: Int     // número del día del mes
)

/** Una sesión planificada en la semana (cada tarjeta de 4.2). */
data class PlannedSession(
    val id: String,                    // id del entrenamiento: se usa para abrir el detalle (4.3)
    val dayLabel: String,              // "TODAY'S WORKOUT", "WEDNESDAY", ...
    val title: String,                 // nombre del entrenamiento
    val details: String,               // duración, ejercicios, nivel
    val isToday: Boolean = false,      // true = tarjeta destacada con la etiqueta READY
    val isRecovery: Boolean = false    // true = día de recuperación (punto verde)
)

/** "object" = una sola instancia con los datos fijos de demostración. */
object WorkoutDemoData {

    /** Semana que muestra el selector de 4.2. */
    val weekDays = listOf(
        PlanDay("MON", 7),
        PlanDay("TUE", 8),
        PlanDay("WED", 9),
        PlanDay("THU", 10),
        PlanDay("FRI", 11),
        PlanDay("SAT", 12),
        PlanDay("SUN", 13)
    )

    /** Posición de "hoy" en weekDays (TUE = 1, porque las listas empiezan en 0). */
    const val TODAY_INDEX = 1

    /** Sesiones de la semana (tarjetas de 4.2). */
    val plannedSessions = listOf(
        PlannedSession(
            id = "w-upper-body",
            dayLabel = "TODAY'S WORKOUT",
            title = "Upper Body Strength",
            details = "42 min • 5 exercises • Intermediate",
            isToday = true
        ),
        PlannedSession(
            id = "w-active-recovery",
            dayLabel = "WEDNESDAY",
            title = "Active Recovery",
            details = "25 min • Mobility • Walk",
            isRecovery = true
        ),
        PlannedSession(
            id = "w-lower-body",
            dayLabel = "THURSDAY",
            title = "Lower Body Strength",
            details = "45 min • 6 exercises"
        )
    )

    /** Texto de la tarjeta oscura "Need to adjust your week?". */
    const val ADJUST_WEEK_MESSAGE =
        "Tell VORA what changed (e.g., sleep, muscle soreness, or schedule cuts) " +
                "and your coach will realign the week dynamically."

    /** Sugerencias rápidas para hablar con VORA. */
    val adjustSuggestions = listOf("I'm tired today", "Move Wed workout to Fri")
}
