package com.bruno13palhano.data.database

import app.cash.turbine.test
import com.bruno13palhano.data.local.data.dao.CharacterDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.mocks.makeRandomCharacter
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
internal class CharacterLocalDataTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: HQsMarvelDatabase
    private lateinit var characterDao: CharacterDao

    @Before
    fun setup() {
        hiltRule.inject()

        characterDao = database.characterDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldInsertCharacterCharacterIntoTheDatabase() =
        runTest {
            val character = makeRandomCharacter()

            characterDao.insertCharacter(character)

            characterDao.getCharacterById(character.id).test {
                val characterResult = awaitItem()

                assertThat(characterResult).isEqualTo(character)
                cancel()
            }
        }

    @Test
    fun shouldNotInsertCharacterCharacterIntoTheDatabaseIfItAlreadyExists() =
        runTest {
            val character1 = makeRandomCharacter()
            val character2 = character1.copy(description = "description")

            characterDao.insertCharacter(character1)
            characterDao.insertCharacter(character2)

            characterDao.getCharacterById(character2.id).test {
                val characterResult = awaitItem()

                assertThat(characterResult).isNotEqualTo(character2)
                cancel()
            }
        }

    @Test
    fun shouldReturnNullWhenThereAreNoCharacterInDatabase() =
        runTest {
            characterDao.getCharacterById(id = 1L).test {
                val characterResult = awaitItem()

                assertThat(characterResult).isNull()
                cancel()
            }
        }

    @Test
    fun shouldReturnTrueIfCharacterExists() =
        runTest {
            val character = makeRandomCharacter()

            characterDao.insertCharacter(character)

            assertThat(characterDao.characterExists(character.id)).isTrue()
        }

    @Test
    fun shouldReturnFalseIfCharacterDoesNotExist() =
        runTest {
            assertThat(characterDao.characterExists(id = 1L)).isFalse()
        }
}