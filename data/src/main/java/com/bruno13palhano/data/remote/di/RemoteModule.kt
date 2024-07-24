package com.bruno13palhano.data.remote.di

import com.bruno13palhano.data.remote.datasource.character.CharacterRemoteDataSource
import com.bruno13palhano.data.remote.datasource.character.DefaultCharacterRemoteDataSource
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import com.bruno13palhano.data.remote.datasource.comics.DefaultComicRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
internal annotation class DefaultComicRemote

@Qualifier
internal annotation class DefaultCharacterRemote

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteModule {
    @DefaultComicRemote
    @Binds
    abstract fun bindComicRemoteDataSource(comicRemoteDataSource: DefaultComicRemoteDataSource): ComicRemoteDataSource

    @DefaultCharacterRemote
    @Binds
    abstract fun bindCharacterRemoteDataSource(characterRemoteDataSource: DefaultCharacterRemoteDataSource): CharacterRemoteDataSource
}