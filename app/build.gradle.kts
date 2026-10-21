// =====================================================================================
// app/build.gradle.kts - Configuración del módulo de la aplicación VORA Coach
// (Estructura alineada con el proyecto de ejemplo de la cátedra.)
// =====================================================================================
import java.util.Properties

plugins {
    // Convierte este módulo en una app Android instalable (.apk). En AGP 9 Kotlin viene incluido.
    alias(libs.plugins.android.application)
    // Habilita el compilador de Compose para las funciones @Composable.
    alias(libs.plugins.kotlin.compose)
    // KSP procesa las anotaciones de Room y Hilt y genera el código en build/generated/ksp.
    alias(libs.plugins.ksp)
    // Plugin de Hilt: necesario para @HiltAndroidApp y @AndroidEntryPoint.
    alias(libs.plugins.hilt.android)
}

// api.properties (raíz del repo) dice qué servidor usa la app y define las dos URL base.
val apiProperties = Properties().apply {
    rootProject.file("api.properties").inputStream().use { load(it) }
}

// vora.server=mock -> mock local (vora.mockBaseUrl); vora.server=api -> cátedra (vora.apiBaseUrl).
val apiServer: String = apiProperties.getProperty("vora.server", "mock").trim()
val baseUrlKey: String = when (apiServer) {
    "mock" -> "vora.mockBaseUrl"
    "api" -> "vora.apiBaseUrl"
    else -> error("api.properties: vora.server debe ser mock o api (vale '$apiServer')")
}

// Se fuerza la barra final porque Retrofit la exige.
val baseUrl: String =
    (apiProperties.getProperty(baseUrlKey)?.trim() ?: error("api.properties: falta $baseUrlKey"))
        .let { if (it.endsWith("/")) it else "$it/" }

android {
    // Paquete base de las clases generadas (R, BuildConfig).
    namespace = "ar.edu.ort.voracoach"
    // Versión del SDK con la que se compila (API 36 = Android 16). Sintaxis nueva de AGP 9.
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        // Identificador único de la app en el dispositivo / Play Store.
        applicationId = "ar.edu.ort.voracoach"
        // Android 7.0 como mínimo (igual que el ejemplo de la cátedra).
        minSdk = 24
        // targetSdk: comportamiento de runtime con el que la app fue probada.
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // buildConfigField crea la constante BuildConfig.BASE_URL accesible desde Kotlin.
        // Así el NetworkModule no tiene la URL "hardcodeada" y se cambia desde api.properties.
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        release {
            // Sin ofuscación para que el docente pueda inspeccionar el APK fácilmente.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Versión de bytecode Java generada (igual que el ejemplo de la cátedra).
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        // Activa Jetpack Compose en el módulo.
        compose = true
        // Genera la clase BuildConfig (la usamos para BASE_URL).
        buildConfig = true
    }
}

dependencies {
    // --- AndroidX base ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- Jetpack Compose + Material 3 (el BOM decide las versiones) ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // --- ViewModel + StateFlow en Compose ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // collectAsStateWithLifecycle(): observa Flows respetando el ciclo de vida de la pantalla.
    implementation(libs.androidx.lifecycle.runtime.compose)

    // --- Navigation Compose: NavHost, NavController y rutas entre las 37 pantallas ---
    implementation(libs.androidx.navigation.compose)

    // --- Hilt / Dagger (RF08) ---
    implementation(libs.hilt.android)
    // ksp(...) = procesador de anotaciones: genera los componentes de Dagger en compilación.
    ksp(libs.hilt.compiler)
    // hiltViewModel(): obtiene ViewModels @HiltViewModel dentro de pantallas Compose.
    implementation(libs.androidx.hilt.navigation.compose)

    // --- Retrofit + OkHttp + Gson (RF01, RF05) ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // --- Room (RF02, RF03, RF04) ---
    implementation(libs.androidx.room.runtime)
    // room-ktx: soporte de corrutinas (suspend) y Flow en los DAO.
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
