package com.bruno13palhano.data.local.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.local.data.ComicsDao
import com.bruno13palhano.data.local.data.FavoriteComicsDao
import com.bruno13palhano.data.local.data.RemoteKeysDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
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
    fun provideComicsDao(database: HQsMarvelDatabase): ComicsDao = database.comicsDao

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: HQsMarvelDatabase): RemoteKeysDao = database.remoteKeysDao

    @Provides
    @Singleton
    fun provideFavoriteComicsDao(database: HQsMarvelDatabase): FavoriteComicsDao = database.favoriteComicsDao

    @Provides
    @Singleton
    fun provideHQsMarvelDatabase(
        @ApplicationContext context: Context
    ): HQsMarvelDatabase {
        return Room.databaseBuilder(
            context,
            HQsMarvelDatabase::class.java,
            "HQsMarvel.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}