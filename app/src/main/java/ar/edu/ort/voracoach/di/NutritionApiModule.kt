package ar.edu.ort.voracoach.di

import ar.edu.ort.voracoach.data.remote.NutritionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Separado de NetworkModule para que Nutrición y Entrenamiento no editen el mismo archivo.
 * Usa el mismo Retrofit (URL, OkHttp e interceptor del mock).
 */
@Module
@InstallIn(SingletonComponent::class)
object NutritionApiModule {

    @Provides
    @Singleton
    fun provideNutritionApiService(retrofit: Retrofit): NutritionApiService =
        retrofit.create(NutritionApiService::class.java)
}
