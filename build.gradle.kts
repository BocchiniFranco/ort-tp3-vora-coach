// =====================================================================================
// build.gradle.kts (raíz del proyecto)
// Declara los plugins con "apply false": se descargan y quedan disponibles, pero cada
// módulo decide si los aplica. Así todos los módulos usan la MISMA versión.
// =====================================================================================
plugins {
    // Plugin de Android para módulos de tipo aplicación (AGP 9 ya incluye Kotlin "built-in").
    alias(libs.plugins.android.application) apply false
    // Plugin del compilador de Jetpack Compose.
    alias(libs.plugins.kotlin.compose) apply false
    // KSP: genera código a partir de anotaciones (@Dao, @Entity, @HiltViewModel, @Module...).
    alias(libs.plugins.ksp) apply false
    // Plugin de Hilt: transforma las clases @AndroidEntryPoint/@HiltAndroidApp en compilación.
    alias(libs.plugins.hilt.android) apply false
}
