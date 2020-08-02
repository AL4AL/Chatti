package com.sajjad.application_component.network.component

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
internal object NetworkModule {

    @JvmStatic
    @Provides
    @NetworkScope
    fun provideOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient = okHttpClientBuilder
        .build()

    @JvmStatic
    @Provides
    fun provideOkHttpBuilder(
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)

    @JvmStatic
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }
}