// =====================================================================================
// settings.gradle.kts
// Define DÓNDE busca Gradle los plugins y las librerías, y QUÉ módulos forman el proyecto.
// =====================================================================================

// Repositorios desde donde se descargan los PLUGINS de Gradle (AGP, Kotlin, KSP, Hilt).
pluginManagement {
    repositories {
        // Repositorio de Google: Android Gradle Plugin y librerías AndroidX.
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Maven Central: Kotlin, KSP, Dagger/Hilt, Retrofit, OkHttp, Gson.
        mavenCentral()
        // Portal oficial de plugins de Gradle.
        gradlePluginPortal()
    }
}

// Repositorios desde donde se descargan las LIBRERÍAS que usa la app.
dependencyResolutionManagement {
    // FAIL_ON_PROJECT_REPOS: obliga a declarar repositorios SOLO aquí (buena práctica).
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Nombre del proyecto que muestra Android Studio.
rootProject.name = "VoraCoach"

// Módulo único de la aplicación.
include(":app")
