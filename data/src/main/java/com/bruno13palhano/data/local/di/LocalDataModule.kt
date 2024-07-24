package com.bruno13palhano.data.local.di

import com.bruno13palhano.data.local.data.CharacterLocalData
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.data.RemoteKeysLocalData
import com.bruno13palhano.data.local.data.dao.CharacterDao
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.data.dao.RemoteKeysDao
import com.bruno13palhano.data.local.data.mediator.DefaultMediatorComicLocalData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultComic

@Qualifier
internal annotation class DefaultCharacter

@Qualifier
internal annotation class DefaultRemoteKeys

@Qualifier
internal annotation class DefaultComicMediator

@Qualifier
internal annotation class DefaultCharacterSummary

@InstallIn(SingletonComponent::class)
@Module
internal abstract class LocalDataModule {
    @DefaultComic
    @Singleton
    @Binds
    abstract fun bindComicLocalData(comicLocalData: ComicsDao): ComicLocalData

    @DefaultCharacter
    @Singleton
    @Binds
    abstract fun bindCharacterLocalData(characterLocalData: CharacterDao): CharacterLocalData

    @DefaultComicMediator
    @Singleton
    @Binds
    abstract fun bindMediatorComicLocalData(remoteMediatorComicLocalData: DefaultMediatorComicLocalData): MediatorComicLocalData

    @DefaultCharacterSummary
    @Singleton
    @Binds
    abstract fun bindCharacterSummaryLocalData(characterSummaryLocalData: CharacterSummaryDao): CharacterSummaryLocalData
}