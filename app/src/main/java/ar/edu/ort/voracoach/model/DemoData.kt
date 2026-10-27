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

/** Grupo en el que se muestra la notificación ("TODAY" / "YESTERDAY" en el Figma). */
enum class NotificationSection(val label: String) { TODAY("TODAY"), YESTERDAY("YESTERDAY") }

/**
 * Notificación del Flujo 2.2 (Figma "Home / 02 — Notifications").
 * @property category texto gris superior ("VORA Coach", "Workout Plan"...).
 * @property isUnread true si todavía no fue leída (muestra el punto verde).
 */
data class NotificationItem(
    val id: Int,
    val type: NotificationType,
    val section: NotificationSection,
    val category: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isUnread: Boolean
)

/** Autor de un mensaje del chat con VORA (Flujo 3.5). */
enum class ChatAuthor { USER, VORA }

/**
 * Mensaje del chat con el asistente.
 * @property time hora que muestra la burbuja ("12:41").
 * @property audioDuration si no es null, la burbuja ofrece "Listen to response (0:14)".
 */
data class ChatMessage(
    val id: Long,
    val author: ChatAuthor,
    val text: String,
    val time: String,
    val audioDuration: String? = null
)

/**
 * Resumen del día que muestra el Home (2.1). Valores de demostración tomados del Figma.
 * Los objetivos (goal) se usan para calcular los porcentajes de barras y anillos.
 */
data class DailySnapshot(
    val dailyGoalPercent: Int,
    val caloriesConsumed: Int,
    val caloriesGoal: Int,
    val steps: Int,
    val distanceKm: Double,
    val activeKcal: Int,
    val proteinGrams: Double,
    val proteinGoal: Double,
    val carbsGrams: Double,
    val carbsGoal: Double,
    val fatGrams: Double,
    val fatGoal: Double
)

/** Entrenamiento recomendado para hoy en el Home ("RECOMMENDED WORKOUT"). */
data class TodayWorkoutPreview(
    val title: String,
    val durationMinutes: Int,
    val level: String,
    val exercises: Int,
    val estimatedVolumeKg: Int
)

/**
 * DemoData: ÚNICA fuente de datos de demostración de los flujos 1, 2 y 3.
 * Es un "object" (singleton de Kotlin): no se instancia, se usa como DemoData.alexMorgan.
 * Todos los textos y números coinciden con el Figma oficial (UI01).
 */
object DemoData {

    /** Usuario de ejemplo exigido por la consigna. */
    val alexMorgan = UserProfile(
        fullName = "Alex Morgan",
        firstName = "Alex",
        email = "alex.morgan@email.com",
        initials = "AM",
        goal = "Build lean muscle",
        level = "Intermediate"
    )

    /** "Today's Overview" del Home: 72% Daily Goal, 1,420 / 2,100 kcal, pasos, distancia y kcal activas. */
    val today = DailySnapshot(
        dailyGoalPercent = 72,
        caloriesConsumed = 1420,
        caloriesGoal = 2100,
        steps = 7842,
        distanceKm = 5.6,
        activeKcal = 312,
        proteinGrams = 104.0,
        proteinGoal = 140.0,
        carbsGrams = 152.0,
        carbsGoal = 220.0,
        fatGrams = 48.0,
        fatGoal = 65.0
    )

    /** "RECOMMENDED WORKOUT": Upper Body Strength · 42 min · 5 exercises · Intermediate. */
    val todayWorkout = TodayWorkoutPreview(
        title = "Upper Body Strength",
        durationMinutes = 42,
        level = "Intermediate",
        exercises = 5,
        estimatedVolumeKg = 2340
    )

    /** Recomendación de la IA del Home. */
    const val TIP_ON_TRACK =
        "You're on track. A protein-rich dinner would help you reach your daily target."

    /** Notificaciones del Figma "Home / 02 — Notifications". */
    val notifications = listOf(
        NotificationItem(
            1, NotificationType.COACH, NotificationSection.TODAY, "VORA Coach",
            "Your weekly health review is ready.",
            "We noticed your training consistency improved by 14% over the last week.",
            "2h ago", isUnread = true
        ),
        NotificationItem(
            2, NotificationType.WORKOUT, NotificationSection.TODAY, "Workout Plan",
            "Upper Body Strength is ready.",
            "Equipped for home/gym. Approx. 42 minutes.",
            "4h ago", isUnread = true
        ),
        NotificationItem(
            3, NotificationType.NUTRITION, NotificationSection.TODAY, "Nutrition Goal",
            "Protein target milestone alert.",
            "You require 36g more of protein to hit today's UK performance baseline.",
            "6h ago", isUnread = false
        ),
        NotificationItem(
            4, NotificationType.GOAL, NotificationSection.YESTERDAY, "Milestone Reached",
            "4-day workout streak logged!",
            "Outstanding work, Alex. Let's make it 5 today.",
            "1d ago", isUnread = false
        )
    )

    // ---------------------------------------------------------------------------------
    // Asistente VORA (Flujo 3) - diálogo simulado del Figma
    // ---------------------------------------------------------------------------------

    /** Lo que el usuario "dice" en Listening (3.2) y se repite en Processing (3.3). */
    const val COACH_DEMO_QUESTION = "I had chicken, rice and some vegetables for lunch."

    /** Respuesta hablada de Speaking (3.4). */
    const val COACH_DEMO_ANSWER =
        "That sounds like a balanced lunch. I've estimated around 520 calories with approximately 38 grams of protein."

    /** Texto del indicador de carga en Processing (3.3). */
    const val COACH_PROCESSING_MESSAGE = "Checking your nutrition target..."

    /** Duración simulada del audio de Speaking, en segundos (0:08 transcurridos + 0:14 restantes). */
    const val COACH_ANSWER_DURATION_SECONDS = 22

    /** "SUGGESTED QUESTIONS" de Voice Idle (3.1). */
    val coachSuggestions = listOf(
        "Start my workout",
        "Log my breakfast",
        "What should I eat tonight?",
        "How am I doing?"
    )

    /** Tarjeta "LAST CONVERSATION" de Voice Idle. */
    const val LAST_CONVERSATION_TEXT = "Create a 30-minute workout for me."
    const val LAST_CONVERSATION_TIME = "2 hours ago"

    /** Encabezado del hilo del chat (3.5). */
    const val CONVERSATION_DATE_LABEL = "TODAY AT 12:40 PM"

    /** Conversación inicial del chat (Figma "Voice / 05 — Conversation"). */
    val initialConversation = listOf(
        ChatMessage(1, ChatAuthor.USER, "What should I eat for dinner?", "12:41"),
        ChatMessage(
            2, ChatAuthor.VORA,
            "You're currently 36g short of your protein target today. I'd highly recommend grilled chicken breast with roasted seasonal vegetables and a side of Greek yoghurt.",
            "12:41", audioDuration = "0:14"
        ),
        ChatMessage(3, ChatAuthor.USER, "Can you make that vegetarian?", "12:42"),
        ChatMessage(
            4, ChatAuthor.VORA,
            "Absolutely. Try grilled organic tofu with roasted vegetables, quinoa and Greek yoghurt.",
            "12:43"
        )
    )

    /**
     * Respuestas "enlatadas" del chat de demostración. Se elige según palabras clave
     * del mensaje del usuario; no hay IA real ni llamadas de red.
     */
    fun cannedReplyFor(userText: String): String {
        val text = userText.lowercase()
        return when {
            "dinner" in text || "eat" in text || "meal" in text || "breakfast" in text ->
                "For dinner, try grilled chicken with sweet potato and greens: about 45g of protein and 550 kcal."
            "workout" in text || "train" in text || "exercise" in text ->
                "Today's plan is Upper Body Strength (42 min, 5 exercises). Want me to start it for you?"
            "doing" in text || "progress" in text ->
                "You're at 72% of today's goal: 1,420 of 2,100 kcal and 7,842 steps. Great consistency!"
            "sleep" in text ->
                "You slept 7h 12m last night, 20 minutes more than your weekly average. Nice!"
            else ->
                "Got it! Based on your goal to build lean muscle, keep protein high and stay consistent with your plan."
        }
    }
}
