package com.bruno13palhano.data.repository.character

import com.bruno13palhano.data.local.data.CharacterLocalData
import com.bruno13palhano.data.local.di.DefaultCharacter
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.datasource.character.CharacterRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultCharacterRemote
import com.bruno13palhano.data.repository.utils.CodeInfoException
import com.bruno13palhano.data.repository.utils.ErrorCode
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class DefaultCharacterRepository
    @Inject
    constructor(
        @DefaultCharacterRemote private val characterRemoteDataSource: CharacterRemoteDataSource,
        @DefaultCharacter private val characterLocalData: CharacterLocalData
    ) : CharacterRepository {
        override suspend fun getCharacter(id: Long): Flow<Character?> {
            return try {
                if (!characterLocalData.characterExists(id)) {
                    val character = characterRemoteDataSource.getCharacter(id = id)
                    characterLocalData.insert(character = character)
                }

                characterLocalData.getCharacter(id = id)
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        throw CodeInfoException(
                            code = ErrorCode.HTTP_ITEM_NOT_FOUND.toString(),
                            cause = e
                        )
                    }

                    else -> {
                        throw CodeInfoException(
                            code = ErrorCode.OTHER_HTTP_ERRORS.toString(),
                            cause = e
                        )
                    }
                }
            } catch (e: IOException) {
                throw CodeInfoException(
                    code = ErrorCode.NETWORK_ERROR.toString(),
                    cause = e
                )
            } catch (e: Exception) {
                throw CodeInfoException(
                    code = ErrorCode.UNEXPECTED_ERROR.toString(),
                    cause = e
                )
            }
        }
    }