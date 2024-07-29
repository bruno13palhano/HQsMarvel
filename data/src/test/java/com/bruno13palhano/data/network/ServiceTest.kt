package com.bruno13palhano.data.network

import com.bruno13palhano.data.BuildConfig
import com.bruno13palhano.data.mocks.JSONFactory
import com.bruno13palhano.data.mocks.getRandomInt
import com.bruno13palhano.data.mocks.getRandomLong
import com.bruno13palhano.data.mocks.makeRandomCharacterDataWrapper
import com.bruno13palhano.data.mocks.makeRandomComicDataWrapper
import com.bruno13palhano.data.remote.Service
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.MessageDigest
import java.util.Calendar
import java.util.TimeZone

class ServiceTest {
    @get:Rule
    val mockWebService = MockWebServer()
    private val moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    private val ts = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis / 1000L).toString()
    private val hash = md5("$ts${BuildConfig.apiPrivateKey}${BuildConfig.apiPublicKey}")
    private val authParams = "ts=$ts&apikey=${BuildConfig.apiPublicKey}&hash=$hash"

    private val id = getRandomLong()
    private val offset = getRandomInt()
    private val limit = getRandomInt()
    private val characterDataWrapper = makeRandomCharacterDataWrapper()
    private val comicDataWrapper = makeRandomComicDataWrapper()
    private val characterDataWrapperJSON = JSONFactory.makeCharacterDataWrapperJSON(characterDataWrapper)
    private val comicDataWrapperJSON = JSONFactory.makeComicDataWrapperJSON(comicDataWrapper)

    private val httpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val baseUrl = original.url

                val url =
                    baseUrl.newBuilder()
                        .addQueryParameter("ts", ts)
                        .addQueryParameter("apikey", BuildConfig.apiPublicKey)
                        .addQueryParameter("hash", hash)
                        .build()

                chain.proceed(original.newBuilder().url(url).build())
            }
            .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebService.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
    }

    private val testApiService: Service by lazy { retrofit.create(Service::class.java) }

    @Test
    fun checkIfGetCharacterPathIsCorrect() =
        runTest {
            mockWebService.enqueue(
                MockResponse()
                    .setBody(characterDataWrapperJSON)
                    .setResponseCode(200)
            )

            testApiService.getCharacter(id = id)

            Assert.assertEquals(
                "/characters/$id?$authParams",
                mockWebService.takeRequest().path
            )
        }

    @Test
    fun getCharacterShouldReturnCharacterDataWrapper() =
        runTest {
            mockWebService.enqueue(
                MockResponse()
                    .setBody(characterDataWrapperJSON)
                    .setResponseCode(200)
            )

            val response = testApiService.getCharacter(id = id)

            Assert.assertEquals(response, characterDataWrapper)
        }

    @Test
    fun checkIfGetComicsPathIsCorrect() =
        runTest {
            mockWebService.enqueue(
                MockResponse()
                    .setBody(comicDataWrapperJSON)
                    .setResponseCode(200)
            )

            testApiService.getComics(offset = offset, limit = limit)

            Assert.assertEquals(
                "/comics?offset=$offset&limit=$limit&$authParams",
                mockWebService.takeRequest().path
            )
        }

    @Test
    fun getComicsShouldReturnComicDataWrapper() =
        runTest {
            mockWebService.enqueue(
                MockResponse()
                    .setBody(comicDataWrapperJSON)
                    .setResponseCode(200)
            )

            val response = testApiService.getComics(offset = offset, limit = limit)

            Assert.assertEquals(response, comicDataWrapper)
        }

    @OptIn(ExperimentalStdlibApi::class)
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.toHexString()
    }
}