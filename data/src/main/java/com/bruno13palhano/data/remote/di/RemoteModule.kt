package com.bruno13palhano.data.remote.di

import com.bruno13palhano.data.remote.datasource.character.CharacterRemote
import com.bruno13palhano.data.remote.datasource.character.DefaultCharacterRemote
import com.bruno13palhano.data.remote.datasource.comics.ComicRemote
import com.bruno13palhano.data.remote.datasource.comics.DefaultComicRemote
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
internal annotation class ComicRemoteSource

@Qualifier
internal annotation class CharacterRemoteSource

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteModule {
    @ComicRemoteSource
    @Binds
    abstract fun bindComicRemote(comicRemote: DefaultComicRemote): ComicRemote

    @CharacterRemoteSource
    @Binds
    abstract fun bindCharacterRemote(characterRemote: DefaultCharacterRemote): CharacterRemote
}