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

        val retrofit =
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BuildConfig.baseUrl)
                .client(
                    OkHttpClient
                        .Builder()
                        .addInterceptor(logger)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .build()
                )
                .build()

        val apiService: Service by lazy { retrofit.create(Service::class.java) }

        return apiService
    }
}