package ar.edu.ort.voracoach.ui.screens.nutrition

import ar.edu.ort.voracoach.model.Meal
import java.util.Locale
import kotlin.math.roundToInt

/** 1420.0 -> "1,420", como en el Figma. */
internal fun Double.thousands(): String = String.format(Locale.US, "%,d", roundToInt())

/**
 * Subtítulo de cada ítem. El Figma muestra la porción ("200g"), pero el registro manual
 * guarda los macros y no el peso, así que se muestran los macros.
 */
internal fun Meal.macrosLabel(): String =
    "P ${protein.roundToInt()}g · C ${carbs.roundToInt()}g · F ${fat.roundToInt()}g"
