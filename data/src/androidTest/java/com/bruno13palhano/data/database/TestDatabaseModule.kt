package com.bruno13palhano.data.database

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
internal object TestDatabaseModule {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ): HQsMarvelDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            HQsMarvelDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
}