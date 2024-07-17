package com.bruno13palhano.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IOScope

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesScopeModule {
    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @Dispatcher(HQsMarvelDispatchers.DEFAULT) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    @Singleton
    @IOScope
    @Provides
    fun providesIOScope(
        @Dispatcher(HQsMarvelDispatchers.IO) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}