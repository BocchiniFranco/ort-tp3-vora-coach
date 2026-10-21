package ar.edu.ort.voracoach.di

import ar.edu.ort.voracoach.data.remote.NutritionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Receta de Hilt para el NutritionApiService (RF08).
 *
 * Está en un módulo aparte del NetworkModule para que Nutrición y Entrenamiento no
 * editen el mismo archivo. Usa el MISMO Retrofit (misma URL, OkHttp e interceptor del mock),
 * que Hilt toma de NetworkModule.provideRetrofit.
 */
@Module
@InstallIn(SingletonComponent::class)
object NutritionApiModule {

    /** retrofit.create(...) genera la implementación de la interface automáticamente. */
    @Provides
    @Singleton
    fun provideNutritionApiService(retrofit: Retrofit): NutritionApiService =
        retrofit.create(NutritionApiService::class.java)
}
