package com.bruno13palhano.data.repository.character

import com.bruno13palhano.data.di.IOScope
import com.bruno13palhano.data.local.data.CharacterLocalData
import com.bruno13palhano.data.local.di.DefaultCharacter
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.datasource.character.CharacterRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultCharacterRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultCharacterRepository
    @Inject
    constructor(
        @DefaultCharacterRemote private val characterRemoteDataSource: CharacterRemoteDataSource,
        @DefaultCharacter private val characterLocalData: CharacterLocalData,
        @IOScope private val ioScope: CoroutineScope
    ) : CharacterRepository {
        override fun getCharacter(id: Long): Flow<Character> {
            try {
                ioScope.launch {
                    if (!characterLocalData.characterExists(id)) {
                        val character = characterRemoteDataSource.getCharacter(id = id)
                        characterLocalData.insert(character = character)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return characterLocalData.getCharacter(id = id)
        }
    }