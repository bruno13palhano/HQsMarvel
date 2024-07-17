package com.bruno13palhano.data.local.di

import android.content.Context
import cache.FavoriteComicsQueries
import com.bruno13palhano.cache.AppDatabase
import com.bruno13palhano.data.local.database.DatabaseFactory
import com.bruno13palhano.data.local.database.DriverFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return DatabaseFactory(driverFactory = DriverFactory(context)).createDatabase()
    }

    @Provides
    @Singleton
    fun provideFavoriteComicsTable(database: AppDatabase): FavoriteComicsQueries = database.favoriteComicsQueries
}