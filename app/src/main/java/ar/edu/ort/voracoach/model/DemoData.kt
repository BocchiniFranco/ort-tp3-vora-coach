// Paquete model: clases de DOMINIO limpias (sin anotaciones de Room, Retrofit ni Android).
// La UI solo conoce estos modelos; nunca DTOs ni Entities.
package ar.edu.ort.voracoach.model

/**
 * Usuario de la app. En este TP es SIEMPRE el usuario de demostración "Alex Morgan":
 * los flujos de Acceso, Inicio, Asistente, Progreso y Perfil no tienen backend real
 * (la consigna prohíbe crear endpoints falsos de login).
 */
data class UserProfile(
    val fullName: String,
    val firstName: String,
    val email: String,
    val initials: String,
    val goal: String,
    val level: String
)

/** Categoría de una notificación: define su ícono y la pantalla a la que lleva (NAV01). */
enum class NotificationType { WORKOUT, NUTRITION, COACH, PROGRESS, GOAL }

/**
 * Notificación del Flujo 2.2.
 * @property isUnread true si todavía no fue leída (muestra el punto verde).
 */
data class NotificationItem(
    val id: Int,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isUnread: Boolean
)

/** Autor de un mensaje del chat con VORA (Flujo 3.5). */
enum class ChatAuthor { USER, VORA }

/** Mensaje del chat con el asistente. */
data class ChatMessage(
    val id: Long,
    val author: ChatAuthor,
    val text: String
)

/**
 * Resumen diario que muestra el Home (2.1). Valores de demostración tomados del diseño.
 * Los objetivos (goal) se usan para calcular los porcentajes de las barras y anillos.
 */
data class DailySnapshot(
    val dailyGoalPercent: Int,
    val caloriesConsumed: Int,
    val caloriesGoal: Int,
    val workoutMinutes: Int,
    val steps: Int,
    val proteinGrams: Double,
    val proteinGoal: Double,
    val carbsGrams: Double,
    val carbsGoal: Double,
    val fatGrams: Double,
    val fatGoal: Double
)

/** Entrenamiento sugerido para hoy en el Home (datos de demostración). */
data class TodayWorkoutPreview(
    val title: String,
    val durationMinutes: Int,
    val level: String,
    val exercises: Int
)

/**
 * DemoData: ÚNICA fuente de datos de demostración de los flujos 1, 2 y 3.
 * Es un "object" (singleton de Kotlin): no se instancia, se usa como DemoData.alexMorgan.
 * Centralizarlo evita "números mágicos" repetidos en las pantallas.
 */
object DemoData {

    /** Usuario de ejemplo exigido por la consigna. */
    val alexMorgan = UserProfile(
        fullName = "Alex Morgan",
        firstName = "Alex",
        email = "alex.morgan@vora.app",
        initials = "AM",
        goal = "Build lean muscle",
        level = "Intermediate"
    )

    /** Resumen del día ("72% Daily Goal", macros Protein 104/140, Carbs 152/220, Fat 48/65). */
    val today = DailySnapshot(
        dailyGoalPercent = 72,
        caloriesConsumed = 1480,
        caloriesGoal = 2200,
        workoutMinutes = 45,
        steps = 8432,
        proteinGrams = 104.0,
        proteinGoal = 140.0,
        carbsGrams = 152.0,
        carbsGoal = 220.0,
        fatGrams = 48.0,
        fatGoal = 65.0
    )

    /** Entrenamiento del día ("READY FOR TODAY? / Upper Body Strength"). */
    val todayWorkout = TodayWorkoutPreview(
        title = "Upper Body Strength",
        durationMinutes = 45,
        level = "Intermediate",
        exercises = 6
    )

    /** Recomendación de la IA que se repite en Home y Nutrition. */
    const val TIP_ON_TRACK =
        "You're on track. A protein-rich dinner would help you reach your daily target."

    /** Notificaciones iniciales del Flujo 2.2. */
    val notifications = listOf(
        NotificationItem(1, NotificationType.WORKOUT, "Time to train", "Upper Body Strength is scheduled for today. 45 min.", "10 min ago", true),
        NotificationItem(2, NotificationType.COACH, "Vora has a suggestion", "Add a protein-rich dinner to reach your 140g target.", "1 h ago", true),
        NotificationItem(3, NotificationType.NUTRITION, "Log your lunch", "You haven't logged lunch yet. Keep your streak going!", "3 h ago", false),
        NotificationItem(4, NotificationType.GOAL, "Goal reached", "You hit 10,000 steps three days in a row.", "Yesterday", false),
        NotificationItem(5, NotificationType.PROGRESS, "Your weekly review is ready", "See how this week compares with the last one.", "2 days ago", false)
    )

    // ---------------------------------------------------------------------------------
    // Asistente VORA (Flujo 3) - diálogo simulado
    // ---------------------------------------------------------------------------------

    /** Pregunta que "transcribe" la pantalla Listening (3.2). */
    const val COACH_DEMO_QUESTION = "How can I hit my protein goal at dinner?"

    /** Respuesta hablada que muestra Voice Response (3.4). */
    const val COACH_DEMO_ANSWER =
        "You've had 104g of protein today, so you need about 36g more. " +
            "A dinner with 150g of grilled salmon and a cup of quinoa adds around 40g, " +
            "which gets you to your 140g target."

    /** Pasos que se animan mientras VORA "piensa" (3.3). */
    val coachProcessingSteps = listOf(
        "Reviewing today's meals",
        "Checking your macro targets",
        "Preparing a recommendation"
    )

    /** Sugerencias rápidas (chips) del asistente. */
    val coachSuggestions = listOf(
        "Plan my dinner",
        "Adjust today's workout",
        "How did I sleep?",
        "Motivate me"
    )

    /** Conversación inicial del chat (3.5). */
    val initialConversation = listOf(
        ChatMessage(1, ChatAuthor.VORA, "Hi Alex! I'm Vora, your AI coach. How can I help you today?"),
        ChatMessage(2, ChatAuthor.USER, COACH_DEMO_QUESTION),
        ChatMessage(3, ChatAuthor.VORA, COACH_DEMO_ANSWER)
    )

    /**
     * Respuestas "enlatadas" del chat de demostración. Se elige según palabras clave
     * del mensaje del usuario; no hay IA real ni llamadas de red.
     */
    fun cannedReplyFor(userText: String): String {
        val text = userText.lowercase()
        return when {
            "dinner" in text || "eat" in text || "meal" in text ->
                "For dinner, try grilled chicken with sweet potato and greens: about 45g of protein and 550 kcal."
            "workout" in text || "train" in text || "exercise" in text ->
                "Today's plan is Upper Body Strength (45 min). If you're tired, drop one set per exercise and keep the form strict."
            "sleep" in text ->
                "You slept 7h 12m last night, 20 minutes more than your weekly average. Nice!"
            "motivat" in text ->
                "You've trained 4 days in a row. Consistency beats intensity: let's make it 5!"
            else ->
                "Got it! Based on your goal to build lean muscle, keep protein high and stay consistent with your plan."
        }
    }
}
