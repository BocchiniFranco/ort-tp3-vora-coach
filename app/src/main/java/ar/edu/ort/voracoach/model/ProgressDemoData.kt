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

    // ---------------------------- 6.3 Workout Progress ----------------------------

    val workoutStats = listOf(
        ProgressStat("SESSIONS", "4"),
        ProgressStat("VOLUME", "8,420 kg"),
        ProgressStat("AVG TIME", "41 min")
    )

    /** Kilos levantados por sesión (barras del gráfico). */
    val trainingVolume = listOf(2600f, 2800f, 300f, 2700f, 3000f)
    val trainingVolumeLabels = listOf("M", "T", "W", "T", "F")
    const val VOLUME_CHANGE = "+8% vs last week"

    const val PERSONAL_BEST_TITLE = "Seated Cable Row"
    const val PERSONAL_BEST_DETAIL = "45 kg × 12 reps (+5 kg vs prev best)"

    const val WORKOUT_PROGRESS_TIP =
        "Your training volume increased without adding workout duration. That's efficiency improving."

    val exerciseTrends = listOf(
        ExerciseTrend("Bench Press", "40 kg × 10 reps", "↑ 2.5 kg", isImproving = true),
        ExerciseTrend("Cable Row", "45 kg × 12 reps", "↑ 5.0 kg", isImproving = true),
        ExerciseTrend("Shoulder Press", "20 kg × 10 reps", "→ Stable", isImproving = false),
        ExerciseTrend("Lateral Raise", "8 kg × 12 reps", "↑ 1.0 kg", isImproving = true)
    )

    // --------------------------- 6.4 Nutrition Progress ---------------------------

    val nutritionStats = listOf(
        ProgressStat("AVG CALORIES", "1,980 kcal"),
        ProgressStat("AVG PROTEIN", "132g"),
        ProgressStat("TARGET HIT", "5 / 7 days")
    )

    val macroTabs = listOf("Calories", "Protein", "Carbs", "Fat")
    const val PROTEIN_TARGET_LABEL = "Daily avg protein target (140g)"

    val proteinDays = listOf(
        DayTarget("M", isHit = true),
        DayTarget("T", isHit = true),
        DayTarget("W", isHit = false),
        DayTarget("T", isHit = true),
        DayTarget("F", isHit = true),
        DayTarget("S", isHit = false),
        DayTarget("S", isHit = true)
    )

    const val NUTRITION_ANALYSIS =
        "\"Your protein intake has improved this week, especially on workout days. Keeping " +
                "this consistent will better support your strength goal.\""

    val foodHabits = listOf(
        ProgressStat("Frequent lunch", "Chicken & Rice Bowl"),
        ProgressStat("Top protein source", "Whey & Chicken Breast"),
        ProgressStat("Avg breakfast", "420 kcal"),
        ProgressStat("Avg dinner", "610 kcal")
    )

    // ----------------------------- 6.5 Health Trends -----------------------------

    val healthStats = listOf(
        ProgressStat("AVG STEPS", "7,842"),
        ProgressStat("AVG DISTANCE", "5.6 km"),
        ProgressStat("ACTIVE KCAL", "312 kcal")
    )

    /** Pasos por día de la semana (barras del gráfico). */
    val weeklySteps = listOf(6200f, 7400f, 8600f, 5600f, 9200f, 7800f, 8100f)
    val weekLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    const val STEPS_GOAL = "Goal: 8,000"

    const val HEALTH_TIP =
        "\"Your activity has been more consistent over the last two weeks. Your workout days " +
                "also tend to be your most active days.\""

    val habitSignals = listOf(
        HabitSignal("Sleep Quality", "Improving", isImproving = true),
        HabitSignal("Daily Movement", "Consistent", isImproving = false),
        HabitSignal("Workout Frequency", "Improving", isImproving = true),
        HabitSignal("Protein Intake", "Improving", isImproving = true)
    )

    // ----------------------------- 6.6 Weekly Review ------------------------------

    const val REVIEW_HEADLINE = "\"You had a strong week.\""

    val reviewStats = listOf(
        ProgressStat("WORKOUTS", "4 sessions"),
        ProgressStat("AVG STEPS", "7,842 steps"),
        ProgressStat("PROTEIN", "132g daily")
    )

    val wentWell = listOf(
        ReviewPoint(
            "You stayed consistent",
            "Four completed workouts helped you maintain your weekly training rhythm."
        ),
        ReviewPoint("Your protein improved", "You reached your protein target on 5 of 7 days."),
        ReviewPoint(
            "Your training volume increased",
            "You completed 8% more volume than last week."
        )
    )

    const val IMPROVE_TITLE = "Dinner consistency"
    const val IMPROVE_DETAIL =
        "Your protein intake tends to drop at dinner. Try planning a protein-rich dinner before the evening."

    val nextWeekTips = listOf(
        "Keep your current workout schedule.",
        "Try to reach 140g protein on at least 6 days.",
        "Aim for 8,000+ steps on non-training days."
    )
}