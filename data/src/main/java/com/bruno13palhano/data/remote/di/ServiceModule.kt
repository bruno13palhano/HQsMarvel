package com.bruno13palhano.data.remote.di

import com.bruno13palhano.data.BuildConfig
import com.bruno13palhano.data.remote.Service
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.MessageDigest
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideService(): Service {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logger)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val baseUrl = original.url
            val ts = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis / 1000L).toString()

            val url =
                baseUrl.newBuilder()
                    .addQueryParameter("ts", ts)
                    .addQueryParameter("apikey", BuildConfig.apiPublicKey)
                    .addQueryParameter("hash", md5("$ts${BuildConfig.apiPrivateKey}${BuildConfig.apiPublicKey}"))
                    .build()

            chain.proceed(original.newBuilder().url(url).build())
        }
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)

        val retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .build()

        val apiService: Service by lazy { retrofit.create(Service::class.java) }

        return apiService
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.toHexString()
    }
}