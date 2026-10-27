package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.data.repository.NutritionDataException
import ar.edu.ort.voracoach.data.repository.NutritionRepository
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.model.NutritionGoals
import ar.edu.ort.voracoach.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Registro de comidas del flujo de Nutrición. Cada pantalla recibe su propia instancia;
 * la fecha, el tipo de comida y el id llegan como argumentos de la ruta (si faltan la
 * fecha y el tipo: hoy y el tipo que corresponde a la hora).
 */
@HiltViewModel
class NutritionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NutritionRepository
) : ViewModel() {

    private val initialDate: String =
        savedStateHandle.get<String>(Screen.ARG_DATE)?.takeIf { MealDates.toUtcMillis(it) != null }
            ?: MealDates.today()

    private val initialMealType: MealType =
        savedStateHandle.get<String>(Screen.ARG_MEAL_TYPE)
            ?.let { arg -> MealType.entries.firstOrNull { it.name.equals(arg, ignoreCase = true) } }
            ?: MealDates.suggestedMealType()

    private val _addFoodState = MutableStateFlow(
        AddFoodUiState(form = MealFormState(date = initialDate, mealType = initialMealType))
    )
    val addFoodState: StateFlow<AddFoodUiState> = _addFoodState.asStateFlow()

    private var searchJob: Job? = null

    // ---------------------------------- Día (5.1 y 5.2) ----------------------------------

    private val goals = DemoData.today.let {
        NutritionGoals(it.caloriesGoal.toDouble(), it.proteinGoal, it.carbsGoal, it.fatGoal)
    }

    private val selectedDay = MutableStateFlow(initialDate)

    /**
     * Comidas y totales del día elegido, leídos de Room. Al cambiar de día, flatMapLatest
     * deja de escuchar el día anterior. WhileSubscribed: solo consulta mientras una
     * pantalla lo muestra (5 s de margen para que rotar no reinicie la consulta).
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val dayState: StateFlow<NutritionDayUiState> = selectedDay
        .flatMapLatest { date ->
            combine(repository.observeMeals(date), repository.observeDailyTotals(date)) { meals, totals ->
                NutritionDayUiState(
                    date = date,
                    isToday = date == MealDates.today(),
                    totals = totals,
                    groups = MealType.entries.map { type -> MealGroup(type, meals.filter { it.mealType == type }) },
                    goals = goals,
                    tip = tipFor(totals, isToday = date == MealDates.today())
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NutritionDayUiState(
                date = initialDate,
                isToday = initialDate == MealDates.today(),
                goals = goals,
                tip = tipFor(DailyTotals(), isToday = initialDate == MealDates.today())
            )
        )

    fun selectDay(utcMillis: Long) {
        selectedDay.value = MealDates.fromUtcMillis(utcMillis)
    }

    /** Recomendación simulada de VORA según cómo viene el día. */
    private fun tipFor(totals: DailyTotals, isToday: Boolean): String = when {
        !isToday && totals.mealCount == 0 ->
            "No meals were logged on this day."
        !isToday ->
            "This day you logged ${totals.calories.roundToInt()} of your ${goals.calories.roundToInt()} kcal goal."
        totals.mealCount == 0 ->
            "Log your first meal and VORA will keep track of your daily targets."
        totals.calories > goals.calories ->
            "You're over your calorie goal today. Keep the rest of the day light."
        totals.protein < goals.protein ->
            "You're on track today. A protein-rich dinner would help you reach your target."
        else ->
            "Great job! You've hit your protein target for today."
    }

    // ---------------------------------- Detalle (5.8) ----------------------------------

    private val mealId: Long? = savedStateHandle.get<Long>(Screen.ARG_MEAL_ID)

    /**
     * La comida se identifica por fecha y tipo, no por el id abierto: así, si se elimina
     * ese ítem, el detalle sigue mostrando los demás de la misma comida.
     */
    private val mealGroupKey = MutableStateFlow<Pair<String, MealType>?>(null)
    private val mealNotFound = MutableStateFlow(false)

    init {
        mealId?.let { id ->
            viewModelScope.launch {
                val meal = repository.observeMeal(id).first()
                if (meal == null) mealNotFound.value = true
                else mealGroupKey.value = meal.date to meal.mealType
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val mealDetailState: StateFlow<MealDetailUiState> = combine(mealGroupKey, mealNotFound) { key, notFound ->
        key to notFound
    }
        .flatMapLatest { (key, notFound) ->
            when {
                notFound -> flowOf(MealDetailUiState.NotFound)
                key == null -> flowOf(MealDetailUiState.Loading)
                else -> {
                    val (date, type) = key
                    combine(repository.observeMeals(date), repository.observeDailyTotals(date)) { meals, day ->
                        val items = meals.filter { it.mealType == type }
                        if (items.isEmpty()) {
                            MealDetailUiState.NotFound
                        } else {
                            MealDetailUiState.Content(
                                group = MealGroup(type, items),
                                date = date,
                                time = MealDates.timeLabel(items.minOf { it.createdAt }),
                                totals = DailyTotals(
                                    calories = items.sumOf { it.calories },
                                    protein = items.sumOf { it.protein },
                                    carbs = items.sumOf { it.carbs },
                                    fat = items.sumOf { it.fat },
                                    mealCount = items.size
                                ),
                                goals = goals,
                                tip = proteinTipFor(day)
                            )
                        }
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MealDetailUiState.Loading)

    private fun proteinTipFor(day: DailyTotals): String {
        val missing = (goals.protein - day.protein).roundToInt()
        return if (missing > 0) {
            "Great protein choice. You're now ${missing}g away from your daily protein target."
        } else {
            "Great protein choice. You've reached your daily protein target."
        }
    }

    // ---------------------------- Baja con confirmación (5.8) ----------------------------

    private val _mealPendingDelete = MutableStateFlow<Meal?>(null)

    /** Ítem que espera confirmación. Está en el ViewModel para que el diálogo sobreviva a la rotación. */
    val mealPendingDelete: StateFlow<Meal?> = _mealPendingDelete.asStateFlow()

    fun requestDelete(meal: Meal) {
        _mealPendingDelete.value = meal
    }

    fun dismissDelete() {
        _mealPendingDelete.value = null
    }

    fun confirmDelete() {
        val meal = _mealPendingDelete.value ?: return
        _mealPendingDelete.value = null
        viewModelScope.launch { repository.deleteMeal(meal.id) }
    }

    private val _message = MutableStateFlow<String?>(null)

    /** Mensaje breve para un Snackbar; la pantalla lo marca como mostrado. */
    val message: StateFlow<String?> = _message.asStateFlow()

    fun onMessageShown() {
        _message.value = null
    }

    /** "Duplicate Meal": copia los ítems de la comida al día de hoy, con el mismo tipo. */
    fun duplicateMeal() {
        val content = mealDetailState.value as? MealDetailUiState.Content ?: return
        viewModelScope.launch {
            val today = MealDates.today()
            val failed = content.group.meals
                .map { repository.addMeal(it.copy(date = today)) }
                .any { it.isFailure }
            _message.value = if (failed) {
                "We couldn't duplicate this meal. Please try again."
            } else {
                "${content.group.type.label} added to today's log."
            }
        }
    }

    // ----------------------------------- Hojas de 5.3 -----------------------------------

    fun openManualEntry() = _addFoodState.update { it.copy(sheet = AddFoodSheet.MANUAL) }

    fun openSearch() {
        _addFoodState.update { it.copy(sheet = AddFoodSheet.SEARCH) }
        // La primera vez muestra todos los alimentos, sin esperar a que escriba
        if (!_addFoodState.value.search.hasSearched) search(query = "", debounce = false)
    }

    fun closeSheet() = _addFoodState.update { it.copy(sheet = AddFoodSheet.NONE) }

    // ------------------------------------ Formulario ------------------------------------

    fun onNameChange(value: String) = updateForm { it.copy(name = value, nameError = null) }

    fun onMealTypeChange(value: MealType) = updateForm { it.copy(mealType = value) }

    fun onDateSelected(utcMillis: Long) = updateForm { it.copy(date = MealDates.fromUtcMillis(utcMillis)) }

    fun onCaloriesChange(value: String) = updateForm { it.copy(calories = value, caloriesError = null) }

    fun onProteinChange(value: String) = updateForm { it.copy(protein = value, proteinError = null) }

    fun onCarbsChange(value: String) = updateForm { it.copy(carbs = value, carbsError = null) }

    fun onFatChange(value: String) = updateForm { it.copy(fat = value, fatError = null) }

    fun saveMeal() {
        val form = _addFoodState.value.form
        if (form.isSaving) return

        val calories = parseAmount(form.calories)
        val protein = parseAmount(form.protein)
        val carbs = parseAmount(form.carbs)
        val fat = parseAmount(form.fat)
        val validated = form.copy(
            nameError = if (form.name.isBlank()) "Enter a food name." else null,
            caloriesError = calories.error,
            proteinError = protein.error,
            carbsError = carbs.error,
            fatError = fat.error,
            saveError = null
        )
        val hasErrors = listOf(
            validated.nameError, validated.caloriesError, validated.proteinError,
            validated.carbsError, validated.fatError
        ).any { it != null }
        if (hasErrors) {
            updateForm { validated }
            return
        }

        updateForm { validated.copy(isSaving = true) }
        viewModelScope.launch {
            val meal = Meal(
                name = form.name,
                date = form.date,
                mealType = form.mealType,
                calories = calories.value!!,
                protein = protein.value!!,
                carbs = carbs.value!!,
                fat = fat.value!!
            )
            repository.addMeal(meal).fold(
                onSuccess = {
                    _addFoodState.update { state ->
                        state.copy(
                            sheet = AddFoodSheet.NONE,
                            form = state.form.copy(isSaving = false),
                            isSaved = true
                        )
                    }
                },
                onFailure = {
                    updateForm {
                        it.copy(isSaving = false, saveError = "We couldn't save this meal. Please try again.")
                    }
                }
            )
        }
    }

    fun onSaveHandled() = _addFoodState.update { it.copy(isSaved = false) }

    // ------------------------------------ Búsqueda ------------------------------------

    fun onSearchQueryChange(query: String) {
        _addFoodState.update { it.copy(search = it.search.copy(query = query)) }
        search(query, debounce = true)
    }

    fun retrySearch() = search(_addFoodState.value.search.query, debounce = false)

    /** Precarga el formulario con los valores cada 100 g del alimento elegido. */
    fun onFoodSelected(food: Food) {
        _addFoodState.update { state ->
            state.copy(
                sheet = AddFoodSheet.MANUAL,
                form = state.form.copy(
                    name = "${food.name} (100 g)",
                    calories = food.per100g.kcal.toInput(),
                    protein = food.per100g.proteinG.toInput(),
                    carbs = food.per100g.carbsG.toInput(),
                    fat = food.per100g.fatG.toInput(),
                    nameError = null, caloriesError = null, proteinError = null,
                    carbsError = null, fatError = null, saveError = null
                )
            )
        }
    }

    private fun search(query: String, debounce: Boolean) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            // Espera a que el usuario deje de escribir para no pedir una búsqueda por letra
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            _addFoodState.update { it.copy(search = it.search.copy(isLoading = true, error = null)) }
            val result = repository.searchFoods(query, pageSize = SEARCH_PAGE_SIZE)
            _addFoodState.update { state ->
                state.copy(
                    search = result.fold(
                        onSuccess = { page ->
                            state.search.copy(results = page.foods, isLoading = false, hasSearched = true)
                        },
                        onFailure = { error ->
                            state.search.copy(
                                isLoading = false,
                                hasSearched = true,
                                error = (error as? NutritionDataException)?.message
                                    ?: "We couldn't search foods. Check your connection and try again."
                            )
                        }
                    )
                )
            }
        }
    }

    private fun updateForm(transform: (MealFormState) -> MealFormState) =
        _addFoodState.update { it.copy(form = transform(it.form)) }

    private data class ParsedAmount(val value: Double?, val error: String?)

    /** Acepta coma o punto decimal: el teclado numérico puede mostrar cualquiera de los dos. */
    private fun parseAmount(text: String): ParsedAmount {
        if (text.isBlank()) return ParsedAmount(null, "Required.")
        // toDoubleOrNull acepta "NaN" e "Infinity": isFinite los descarta
        val value = text.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() }
            ?: return ParsedAmount(null, "Enter a number.")
        if (value < 0) return ParsedAmount(null, "Can't be negative.")
        return ParsedAmount(value, null)
    }

    private fun Double.toInput(): String =
        if (this % 1.0 == 0.0) toLong().toString() else String.format(Locale.US, "%.1f", this)

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 350L
        const val SEARCH_PAGE_SIZE = 20
    }
}
