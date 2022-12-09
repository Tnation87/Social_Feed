package com.example.social_feed.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UiModule {

    @Provides
    @Singleton
    fun provideOkHttp(
        @Named("ApiTimeout") timeout: Long,
        @Named("Chuck") chuck: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder().run {
            readTimeout(timeout, TimeUnit.MILLISECONDS)
            connectTimeout(timeout, TimeUnit.MILLISECONDS)
            addInterceptor(chuck)
            build()
        }
}
