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

/** Una tarjeta de información antes de entrenar (4.4 Pre Workout). */
data class CoachNote(
    val label: String,   // etiqueta chica arriba, ej. "TODAY'S FOCUS"
    val title: String?,  // título opcional (null = la tarjeta no tiene título)
    val body: String     // texto principal
)

/** Un día de la fila "This Week's Activity" de 4.1. */
data class WeekActivityDay(
    val label: String,             // inicial del día: "M", "T", ...
    val isDone: Boolean,           // true = ya entrenó ese día (círculo verde)
    val isToday: Boolean = false   // true = hoy (círculo oscuro)
)

/** Un ejercicio del entrenamiento activo simulado (4.5). */
data class ActiveExercise(
    val name: String,           // nombre del ejercicio
    val sets: Int,              // cantidad de series
    val reps: Int,              // repeticiones objetivo por serie
    val startWeightKg: Double,  // peso sugerido para empezar
    val lastSession: String     // cómo le fue la última vez, ej. "45 kg × 12 reps"
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
            id = "w01",   // ids reales del mock: cada tarjeta abre SU detalle (NAV04)
            dayLabel = "TODAY'S WORKOUT",
            title = "Fuerza inicial",
            details = "25 min • 3 exercises • Beginner",
            isToday = true
        ),
        PlannedSession(
            id = "w02",
            dayLabel = "WEDNESDAY",
            title = "Entrenamiento completo",
            details = "35 min • 3 exercises • Intermediate",
            isRecovery = true
        ),
        PlannedSession(
            id = "w03",
            dayLabel = "THURSDAY",
            title = "Fuerza y constancia",
            details = "30 min • 3 exercises • Beginner"
        )
    )

    /** Texto de la tarjeta oscura "Need to adjust your week?". */
    const val ADJUST_WEEK_MESSAGE =
        "Tell VORA what changed (e.g., sleep, muscle soreness, or schedule cuts) " +
                "and your coach will realign the week dynamically."

    /** Sugerencias rápidas para hablar con VORA. */
    val adjustSuggestions = listOf("I'm tired today", "Move Wed workout to Fri")


// ------------------------------- 4.4 Pre Workout -------------------------------

    /** Entrenamiento que se está por empezar (el de hoy). */
    const val TODAY_WORKOUT_TITLE = "Upper Body Strength"
    const val TODAY_WORKOUT_SUMMARY = "42 min • 5 exercises"

    /** Tarjetas de 4.4: foco del día, última sesión y consejo del coach. */
    val preWorkoutNotes = listOf(
        CoachNote(
            label = "TODAY'S FOCUS",
            title = "Controlled Repetitions",
            body = "Keep concentric lifts explosive and focus on a slow, 2-second " +
                    "eccentric return for maximal hypertrophy stimulus."
        ),
        CoachNote(
            label = "YOUR LAST SESSION",
            title = "Bench Press Volume",
            body = "Set 1: 40 kg × 10 • Set 2: 40 kg × 9 • Set 3: 37.5 kg × 10"
        ),
        CoachNote(
            label = "VORA'S COACH TIP",
            title = null,
            body = "\"Start slightly lighter on your first set. If your form feels " +
                    "exceptionally robust, increase the load.\""
        )
    )

    /** Sugerencia del botón "Need anything?". */
    const val PRE_WORKOUT_SUGGESTION = "\"Recommend a chest warm-up\""

    // ----------------------------- 4.1 Workout Dashboard -----------------------------

    /** Intensidad del entrenamiento de hoy (píldora de la tarjeta oscura). */
    const val TODAY_WORKOUT_INTENSITY = "MODERATE"

    /** Detalle del entrenamiento de hoy en la tarjeta oscura. */
    const val TODAY_WORKOUT_FOCUS = "42 min • 5 exercises • Focus: Chest & Back"

    /** Recomendación de VORA en el banner menta. */
    const val DASHBOARD_RECOMMENDATION =
        "Your last upper-body session was 4 days ago. Today is a great opportunity " +
                "to hit your strength volume targets."

    /** Fila "This Week's Activity". */
    val weekActivity = listOf(
        WeekActivityDay("M", isDone = true),
        WeekActivityDay("T", isDone = true),
        WeekActivityDay("W", isDone = false),
        WeekActivityDay("T", isDone = true),
        WeekActivityDay("F", isDone = false, isToday = true),
        WeekActivityDay("S", isDone = false),
        WeekActivityDay("S", isDone = false)
    )

    /** Tarjeta "Your Progress". */
    const val PROGRESS_STREAK = "4 Days"
    const val PROGRESS_WORKOUTS = "3/5"
    const val PROGRESS_VOLUME = "8,420 kg"

    // ----------------------------- 4.5 Active Workout -----------------------------

    /** Ejercicios del entrenamiento activo (simulado: no se guarda nada). */
    val activeWorkoutExercises = listOf(
        ActiveExercise(
            "Bench Press",
            sets = 3,
            reps = 10,
            startWeightKg = 40.0,
            lastSession = "40 kg × 10 reps"
        ),
        ActiveExercise(
            "Seated Cable Row",
            sets = 3,
            reps = 12,
            startWeightKg = 45.0,
            lastSession = "45 kg × 12 reps"
        ),
        ActiveExercise(
            "Shoulder Press",
            sets = 3,
            reps = 10,
            startWeightKg = 20.0,
            lastSession = "20 kg × 10 reps"
        )
    )

    /** Frase del coach durante el entrenamiento. */
    const val ACTIVE_WORKOUT_COACH_TIP =
        "\"Two more reps. Keep your chest elevated, brace your core, and control the bar's return.\""
}