// Paquete de navegación: rutas (Screen.kt) y grafo de navegación (VoraNavHost.kt).
package ar.edu.ort.voracoach.navigation

// Uri.encode: codifica parámetros de ruta para que caracteres especiales no rompan la URL interna.
import android.net.Uri

/**
 * Estado de implementación de cada pantalla, usado en el inventario del README (NAV01).
 * - FUNCTIONAL: consume datos reales (Retrofit y/o Room).
 * - DEMO: navegable con datos de demostración locales.
 */
enum class ScreenStatus { FUNCTIONAL, DEMO }

/**
 * Los 7 flujos del diseño de Figma. [number] es el prefijo de cada pantalla (1.x ... 7.x).
 */
enum class VoraFlow(val number: Int, val title: String) {
    ACCESS(1, "Access"),
    HOME(2, "Home"),
    COACH(3, "VORA Assistant"),
    WORKOUT(4, "Workout"),
    NUTRITION(5, "Nutrition"),
    PROGRESS(6, "Progress"),
    PROFILE(7, "Profile")
}

/**
 * Screen: catálogo TIPADO de las 37 pantallas de VORA Coach (7 flujos).
 *
 * ¿Por qué una sealed class?
 *  - El compilador conoce TODAS las subclases: un "when (screen)" sin rama else
 *    obliga a contemplar cada pantalla, y si alguien agrega una nueva, el código que
 *    no la maneja deja de compilar.
 *  - Evita escribir rutas como Strings sueltos por toda la app (errores de tipeo = crash).
 *  - Cada pantalla con argumentos expone una función createRoute(...) que arma la ruta
 *    concreta, de modo que nadie concatena Strings a mano.
 *
 * Cada subclase es un "data object" (Kotlin 1.9+): un singleton con toString() legible.
 *
 * @property route patrón de ruta registrado en el NavHost ("{arg}" = argumento obligatorio,
 *                 "?arg={arg}" = argumento opcional).
 * @property code número de pantalla según Figma (ej. "4.3").
 * @property title nombre del frame de Figma (en inglés, como el diseño).
 * @property flow flujo al que pertenece.
 * @property status Funcional (Retrofit/Room) o Demostración.
 */
sealed class Screen(
    val route: String,
    val code: String,
    val title: String,
    val flow: VoraFlow,
    val status: ScreenStatus = ScreenStatus.DEMO
) {

    // =================================================================================
    // FLUJO 1 - ACCESO (2 pantallas) - Integrante 1
    // =================================================================================
    /** 1.1 Inicio de sesión (demo: no hay autenticación real ni endpoint falso). */
    data object Login : Screen("access/login", "1.1", "Login", VoraFlow.ACCESS)

    /** 1.2 Registro de cuenta (demo). */
    data object Register : Screen("access/register", "1.2", "Register", VoraFlow.ACCESS)

    // =================================================================================
    // FLUJO 2 - INICIO (2 pantallas) - Integrante 1
    // =================================================================================
    /** 2.1 Dashboard principal: destino "Home" de la barra inferior. */
    data object HomeDashboard : Screen("home/dashboard", "2.1", "Home Dashboard", VoraFlow.HOME)

    /** 2.2 Notificaciones (ícono de campana). */
    data object Notifications : Screen("home/notifications", "2.2", "Notifications", VoraFlow.HOME)

    // =================================================================================
    // FLUJO 3 - ASISTENTE VORA (6 estados) - Integrante 1
    // =================================================================================
    /** 3.1 Asistente listo para escuchar. */
    data object CoachReady : Screen("coach/ready", "3.1", "Coach Ready", VoraFlow.COACH)

    /** 3.2 Escuchando (simulado, sin micrófono real). */
    data object CoachListening : Screen("coach/listening", "3.2", "Coach Listening", VoraFlow.COACH)

    /** 3.3 Procesando la consulta. */
    data object CoachProcessing : Screen("coach/processing", "3.3", "Coach Processing", VoraFlow.COACH)

    /** 3.4 Respuesta por voz del asistente. */
    data object CoachVoiceResponse : Screen("coach/voice-response", "3.4", "Coach Voice Response", VoraFlow.COACH)

    /** 3.5 Conversación en formato chat. */
    data object CoachConversation : Screen("coach/conversation", "3.5", "Coach Conversation", VoraFlow.COACH)

    /** 3.6 Error de micrófono (simulado). */
    data object CoachMicError : Screen("coach/mic-error", "3.6", "Coach Mic Error", VoraFlow.COACH)

    // =================================================================================
    // FLUJO 4 - ENTRENAMIENTO (7 pantallas) - Integrante 2
    // =================================================================================
    /** 4.1 Catálogo de entrenamientos: GET /v1/workouts + caché Room (RF01, RF02). */
    data object WorkoutDashboard : Screen(
        "workout/dashboard", "4.1", "Workout Dashboard", VoraFlow.WORKOUT, ScreenStatus.FUNCTIONAL
    )

    /** 4.2 Plan semanal (demo). */
    data object WeeklyPlan : Screen("workout/weekly-plan", "4.2", "Weekly Plan", VoraFlow.WORKOUT)

    /** 4.3 Detalle: GET /v1/workouts/{workoutId} + caché Room (RF01, RF02, RF05). */
    data object WorkoutDetail : Screen(
        "workout/detail/{$ARG_WORKOUT_ID}", "4.3", "Workout Detail", VoraFlow.WORKOUT, ScreenStatus.FUNCTIONAL
    ) {
        /** Arma la ruta concreta, ej. "workout/detail/w-101". */
        fun createRoute(workoutId: String): String = "workout/detail/${Uri.encode(workoutId)}"
    }

    /** 4.4 Preparación antes de entrenar (demo). */
    data object WorkoutPrep : Screen("workout/prep", "4.4", "Workout Prep", VoraFlow.WORKOUT)

    /** 4.5 Entrenamiento activo con cronómetro simulado (demo). */
    data object ActiveWorkout : Screen("workout/active", "4.5", "Active Workout", VoraFlow.WORKOUT)

    /** 4.6 Guía de ejecución del ejercicio (demo). */
    data object ExerciseGuide : Screen("workout/exercise-guide", "4.6", "Exercise Guide", VoraFlow.WORKOUT)

    /** 4.7 Entrenamiento completado (demo). */
    data object WorkoutCompleted : Screen("workout/completed", "4.7", "Workout Completed", VoraFlow.WORKOUT)

    // =================================================================================
    // FLUJO 5 - NUTRICIÓN (8 pantallas) - Integrante 3
    // =================================================================================
    /** 5.1 Dashboard de nutrición con totales del día calculados desde Room (RF04). */
    data object NutritionDashboard : Screen(
        "nutrition/dashboard", "5.1", "Nutrition Dashboard", VoraFlow.NUTRITION, ScreenStatus.FUNCTIONAL
    )

    /** 5.2 Comidas del día (lista por fecha + baja con confirmación) desde Room (RF04). */
    data object DailyMeals : Screen(
        "nutrition/meals/{$ARG_DATE}", "5.2", "Daily Meals", VoraFlow.NUTRITION, ScreenStatus.FUNCTIONAL
    ) {
        /** @param date fecha en formato ISO "yyyy-MM-dd". */
        fun createRoute(date: String): String = "nutrition/meals/${Uri.encode(date)}"
    }

    /** 5.3 Alta manual de comida en Room (RF03). Fecha y tipo son opcionales. */
    data object AddFood : Screen(
        "nutrition/add?$ARG_DATE={$ARG_DATE}&$ARG_MEAL_TYPE={$ARG_MEAL_TYPE}",
        "5.3", "Add Food", VoraFlow.NUTRITION, ScreenStatus.FUNCTIONAL
    ) {
        /** Sin argumentos usa la fecha de hoy y el tipo sugerido por la hora. */
        fun createRoute(date: String? = null, mealType: String? = null): String = buildString {
            append("nutrition/add")
            val params = buildList {
                if (date != null) add("$ARG_DATE=${Uri.encode(date)}")
                if (mealType != null) add("$ARG_MEAL_TYPE=${Uri.encode(mealType)}")
            }
            if (params.isNotEmpty()) append(params.joinToString("&", prefix = "?"))
        }
    }

    /** 5.4 Registro por voz (simulado, sin permiso de micrófono - NAV05). */
    data object VoiceFoodLog : Screen("nutrition/voice-log", "5.4", "Voice Food Log", VoraFlow.NUTRITION)

    /** 5.5 Análisis de alimentos por IA (simulado). */
    data object FoodAnalysis : Screen("nutrition/food-analysis", "5.5", "Food Analysis", VoraFlow.NUTRITION)

    /** 5.6 Análisis de foto (simulado, sin permiso de cámara - NAV05). */
    data object PhotoAnalysis : Screen("nutrition/photo-analysis", "5.6", "Photo Analysis", VoraFlow.NUTRITION)

    /** 5.7 Revisión y confirmación de la comida detectada. */
    data object MealReviewConfirm : Screen("nutrition/review", "5.7", "Meal Review & Confirm", VoraFlow.NUTRITION)

    /** 5.8 Detalle de una comida guardada en Room (con opción de borrar). */
    data object MealDetail : Screen(
        "nutrition/meal/{$ARG_MEAL_ID}", "5.8", "Meal Detail", VoraFlow.NUTRITION, ScreenStatus.FUNCTIONAL
    ) {
        fun createRoute(mealId: Long): String = "nutrition/meal/$mealId"
    }

    // =================================================================================
    // FLUJO 6 - PROGRESO (6 pantallas) - Integrante 2
    // =================================================================================
    /** 6.1 Resumen diario ("Today's Overview"). */
    data object DailySummary : Screen("progress/daily-summary", "6.1", "Daily Summary", VoraFlow.PROGRESS)

    /** 6.2 Dashboard de progreso: destino "Progress" de la barra inferior. */
    data object ProgressDashboard : Screen("progress/dashboard", "6.2", "Progress Dashboard", VoraFlow.PROGRESS)

    /** 6.3 Progreso de entrenamientos. */
    data object WorkoutProgress : Screen("progress/workouts", "6.3", "Workout Progress", VoraFlow.PROGRESS)

    /** 6.4 Progreso de nutrición. */
    data object NutritionProgress : Screen("progress/nutrition", "6.4", "Nutrition Progress", VoraFlow.PROGRESS)

    /** 6.5 Tendencias de salud. */
    data object HealthTrends : Screen("progress/health-trends", "6.5", "Health Trends", VoraFlow.PROGRESS)

    /** 6.6 Revisión semanal. */
    data object WeeklyReview : Screen("progress/weekly-review", "6.6", "Weekly Review", VoraFlow.PROGRESS)

    // =================================================================================
    // FLUJO 7 - PERFIL (6 pantallas) - Integrante 3
    // =================================================================================
    /** 7.1 Perfil de Alex Morgan: destino "Profile" de la barra inferior. */
    data object ProfileDashboard : Screen("profile/dashboard", "7.1", "Profile Dashboard", VoraFlow.PROFILE)

    /** 7.2 Edición de perfil (demo, sin persistencia). */
    data object EditProfile : Screen("profile/edit", "7.2", "Edit Profile", VoraFlow.PROFILE)

    /** 7.3 Objetivos y preferencias. */
    data object GoalsPreferences : Screen("profile/goals", "7.3", "Goals & Preferences", VoraFlow.PROFILE)

    /** 7.4 Preferencias de nutrición. */
    data object NutritionPreferences : Screen("profile/nutrition-preferences", "7.4", "Nutrition Preferences", VoraFlow.PROFILE)

    /** 7.5 Configuración de voz e IA. */
    data object VoiceAiSettings : Screen("profile/voice-ai", "7.5", "Voice & AI Settings", VoraFlow.PROFILE)

    /** 7.6 Configuración de la app, con cierre de sesión que vuelve a Login. */
    data object AppSettings : Screen("profile/app-settings", "7.6", "App Settings", VoraFlow.PROFILE)

    /**
     * companion object: miembros "estáticos" de la clase, accesibles como Screen.X.
     */
    companion object {
        // Nombres de los argumentos de navegación (se reutilizan en NavHost y ViewModels).
        const val ARG_WORKOUT_ID = "workoutId"
        const val ARG_DATE = "date"
        const val ARG_MEAL_TYPE = "mealType"
        const val ARG_MEAL_ID = "mealId"

        /**
         * Lista de las 37 pantallas en orden de Figma.
         * "by lazy": se construye la primera vez que se usa, cuando todos los data object ya
         * están inicializados (evita nulls por orden de inicialización de la sealed class).
         */
        val all: List<Screen> by lazy {
            listOf(
                Login, Register,
                HomeDashboard, Notifications,
                CoachReady, CoachListening, CoachProcessing, CoachVoiceResponse, CoachConversation, CoachMicError,
                WorkoutDashboard, WeeklyPlan, WorkoutDetail, WorkoutPrep, ActiveWorkout, ExerciseGuide, WorkoutCompleted,
                NutritionDashboard, DailyMeals, AddFood, VoiceFoodLog, FoodAnalysis, PhotoAnalysis, MealReviewConfirm, MealDetail,
                DailySummary, ProgressDashboard, WorkoutProgress, NutritionProgress, HealthTrends, WeeklyReview,
                ProfileDashboard, EditProfile, GoalsPreferences, NutritionPreferences, VoiceAiSettings, AppSettings
            )
        }

        /** Rutas de los 5 destinos raíz donde se muestra la VoraBottomBar. */
        val bottomBarRoutes: Set<String> by lazy {
            setOf(
                HomeDashboard.route,
                WorkoutDashboard.route,
                NutritionDashboard.route,
                ProgressDashboard.route,
                ProfileDashboard.route,
                // En el Figma, Notifications también muestra la barra (con Home activo).
                Notifications.route,
                // Y estas pantallas de Nutrición, con Nutrition activo.
                DailyMeals.route,
                AddFood.route,
                PhotoAnalysis.route,
                MealReviewConfirm.route,
                MealDetail.route
            )
        }

        /** Busca la pantalla correspondiente a un patrón de ruta (o null si no existe). */
        fun fromRoute(route: String?): Screen? = all.firstOrNull { it.route == route }
    }
}
