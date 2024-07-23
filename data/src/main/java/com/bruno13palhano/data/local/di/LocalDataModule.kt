package com.bruno13palhano.data.local.di

import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.data.RemoteKeysLocalData
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
internal annotation class DefaultRemoteKeys

@Qualifier
internal annotation class DefaultComicMediator

@InstallIn(SingletonComponent::class)
@Module
internal abstract class LocalDataModule {
    @DefaultComic
    @Singleton
    @Binds
    abstract fun bindComicLocalData(comicLocalData: ComicsDao): ComicLocalData

    @DefaultRemoteKeys
    @Singleton
    @Binds
    abstract fun bindRemoteKeysLocalData(remoteKeysLocalData: RemoteKeysDao): RemoteKeysLocalData

    @DefaultComicMediator
    @Singleton
    @Binds
    abstract fun bindMediatorComicLocalData(remoteMediatorComicLocalData: DefaultMediatorComicLocalData): MediatorComicLocalData
}