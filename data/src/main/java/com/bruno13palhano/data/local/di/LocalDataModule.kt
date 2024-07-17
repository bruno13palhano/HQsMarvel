package com.bruno13palhano.data.local.di

import com.bruno13palhano.data.local.data.DefaultFavoriteComicsLocalData
import com.bruno13palhano.data.local.data.FavoriteComicsLocalData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class FavoriteComicsLocal

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataModule {
    @FavoriteComicsLocal
    @Singleton
    @Binds
    abstract fun bindFavoriteComicsLocalData(favoriteComicsLocalData: DefaultFavoriteComicsLocalData): FavoriteComicsLocalData
}