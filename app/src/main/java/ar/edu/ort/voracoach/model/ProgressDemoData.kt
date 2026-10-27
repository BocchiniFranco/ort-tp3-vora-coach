package ar.edu.ort.voracoach.model

// =====================================================================================
// Datos de DEMOSTRACIÓN del flujo 6 (Progreso) - Integrante 2.
// La consigna permite métricas simuladas para Progreso: nada viene del servidor.
// =====================================================================================

/** Un dato con su etiqueta, ej. "VOLUME" / "8,420 kg". */
data class ProgressStat(val label: String, val value: String)

/** Una fila de "Today's Overview" (6.1). isPending = todavía no empezó (punto rojo). */
data class CompletionItem(
    val label: String,
    val detail: String,
    val percent: String,
    val isPending: Boolean = false
)

/** Una categoría del dashboard (6.2): Workout, Nutrition o Activity. */
data class ProgressCategory(val title: String, val subtitle: String)

/** Progreso de un ejercicio (6.3). isImproving = subió de peso (flecha verde). */
data class ExerciseTrend(
    val name: String,
    val detail: String,
    val change: String,
    val isImproving: Boolean
)

/** Un día del objetivo nutricional (6.4): si cumplió (HIT) o no (MISS). */
data class DayTarget(val label: String, val isHit: Boolean)

/** Una señal de hábito (6.5), ej. "Sleep Quality" / "Improving". */
data class HabitSignal(val name: String, val status: String, val isImproving: Boolean)

/** Un punto de la revisión semanal (6.6), con título y detalle. */
data class ReviewPoint(val title: String, val detail: String)

object ProgressDemoData {

    /** Opciones del selector de período (6.2, 6.3 y 6.4). */
    val periods = listOf("Week", "Month", "3 Months")

    // --------------------------- 6.1 Today's Overview ---------------------------

    const val OVERVIEW_DATE = "TUESDAY, 8 SEPTEMBER"
    const val DAILY_COMPLETION = 0.72f
    const val DAILY_COMPLETION_TEXT = "72%"

    val completionItems = listOf(
        CompletionItem("Workout Progress", "Not started", "0%", isPending = true),
        CompletionItem("Nutrition Target", "1,420 / 2,100 kcal", "78%"),
        CompletionItem("Activity Target", "7,842 steps", "84%"),
        CompletionItem("Hydration Intake", "1.6 / 2.5 Litres", "65%")
    )

    const val OVERVIEW_ADVICE =
        "\"Completing today's intermediate workout and a protein-rich dinner will secure " +
                "your optimal target recovery.\""

    const val OVERVIEW_GOAL = "Build strength"
    const val OVERVIEW_WEEKLY_TARGET = "4 / 5 Workouts"

    // ---------------------------- 6.2 Progress Dashboard -------------------------

    val weekOverview = listOf(
        ProgressStat("Workouts completed", "4 workouts"),
        ProgressStat("Avg Daily Steps", "7,842"),
        ProgressStat("Daily Avg Protein", "104g"),
        ProgressStat("Active Streak", "4 days")
    )

    const val DASHBOARD_INSIGHT =
        "\"You've completed 4 workouts this week and increased your training volume by 8%. " +
                "Your consistency is trending in the right direction.\""

    val categories = listOf(
        ProgressCategory("Workout", "3 sessions • 2,340 kg volume"),
        ProgressCategory("Nutrition", "104g protein • 74% avg target"),
        ProgressCategory("Activity", "7,842 steps • 5.6 km daily avg")
    )

    const val ACTIVE_GOAL = "Build strength & improve fitness"
    const val ACTIVE_GOAL_PROGRESS = "68% complete"
    const val ACTIVE_GOAL_DETAIL = "You're building consistency across workouts and nutrition."
}