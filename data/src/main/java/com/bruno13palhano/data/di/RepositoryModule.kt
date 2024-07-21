package com.bruno13palhano.data.di

import com.bruno13palhano.data.repository.character.CharacterRepository
import com.bruno13palhano.data.repository.character.DefaultCharacterRepository
import com.bruno13palhano.data.repository.comics.ComicsRepository
import com.bruno13palhano.data.repository.comics.DefaultComicsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
annotation class ComicsRep

@Qualifier
annotation class CharacterRep

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @ComicsRep
    @Binds
    abstract fun bindComicsRepository(comicsRepository: DefaultComicsRepository): ComicsRepository

    @CharacterRep
    @Binds
    abstract fun bindCharacterRepository(characterRepository: DefaultCharacterRepository): CharacterRepository
}