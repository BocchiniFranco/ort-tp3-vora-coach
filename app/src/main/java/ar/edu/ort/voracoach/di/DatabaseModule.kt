package ar.edu.ort.voracoach.di

import android.content.Context
import androidx.room.Room
import ar.edu.ort.voracoach.data.local.VoraDatabase
import ar.edu.ort.voracoach.data.local.dao.MealDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Una sola instancia: abrir SQLite es costoso y todos los DAO deben ver los mismos datos. */
    @Provides
    @Singleton
    fun provideVoraDatabase(@ApplicationContext context: Context): VoraDatabase =
        Room.databaseBuilder(context, VoraDatabase::class.java, VoraDatabase.NAME)
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideMealDao(database: VoraDatabase): MealDao = database.mealDao()
}
