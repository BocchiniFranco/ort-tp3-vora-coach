package ar.edu.ort.voracoach.di

import ar.edu.ort.voracoach.BuildConfig
import ar.edu.ort.voracoach.data.remote.MockHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo de Hilt con las "recetas" de red (RF01, RF08).
 *
 * @Module: le dice a Hilt que esta clase contiene recetas (@Provides).
 * @InstallIn(SingletonComponent::class): las recetas valen para toda la app,
 * mientras esté abierta.
 * Es un "object" porque no necesita estado: solo contiene funciones.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** Tiempo máximo de espera de cada pedido (mayor que la demora de prueba del mock). */
    private const val TIMEOUT_SECONDS = 30L

    /**
     * Receta del "cartero" (OkHttpClient).
     * Hilt le pasa el MockHeaderInterceptor solo, porque sabe crearlo (@Inject).
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(mockHeaderInterceptor: MockHeaderInterceptor): OkHttpClient {
        // Muestra en Logcat cada pedido y su respuesta completa (útil para ver el JSON)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(mockHeaderInterceptor) // 1°: agrega los headers del escenario
            .addInterceptor(logging)               // 2°: registra el pedido ya con los headers
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Receta del "traductor" (Retrofit).
     * Pide el OkHttpClient de la receta de arriba: Hilt los conecta solo.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)                     // viene de gradle.properties
            .client(okHttpClient)                              // usa nuestro cartero
            .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
            .build()
}