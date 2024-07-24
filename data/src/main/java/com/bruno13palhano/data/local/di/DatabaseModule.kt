package com.bruno13palhano.data.local.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.local.data.dao.CharacterDao
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicOffsetDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.data.dao.RemoteKeysDao
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
    fun provideCharacterDao(database: HQsMarvelDatabase): CharacterDao = database.characterDao

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: HQsMarvelDatabase): RemoteKeysDao = database.remoteKeysDao

    @Provides
    @Singleton
    fun provideCharacterSummaryDao(database: HQsMarvelDatabase): CharacterSummaryDao = database.characterSummaryDao

    @Provides
    @Singleton
    fun provideComicOffsetDao(database: HQsMarvelDatabase): ComicOffsetDao = database.comicOffsetDao

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