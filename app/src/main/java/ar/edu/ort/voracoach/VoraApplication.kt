// Paquete raíz de la aplicación (coincide con namespace/applicationId de build.gradle.kts).
package ar.edu.ort.voracoach

// Application: clase base de Android que vive mientras el proceso de la app esté activo.
import android.app.Application
// @HiltAndroidApp: anotación de Hilt que dispara la generación de código del contenedor raíz.
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application de VORA Coach (RF08).
 *
 * @HiltAndroidApp le indica a Hilt que genere (en compilación, vía KSP) la clase
 * "Hilt_VoraApplication" con el componente raíz de Dagger: el SingletonComponent.
 * Ese componente vive tanto como la app y guarda las instancias @Singleton que
 * proveen NetworkModule (Retrofit, OkHttp) y DatabaseModule (Room, DAOs).
 *
 * Debe registrarse en AndroidManifest.xml con android:name=".VoraApplication".
 */
@HiltAndroidApp
class VoraApplication : Application()
