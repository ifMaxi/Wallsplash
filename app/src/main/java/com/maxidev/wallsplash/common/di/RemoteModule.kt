package com.maxidev.wallsplash.common.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.API_KEY
import com.maxidev.wallsplash.common.utils.Constants.AUTHORIZATION
import com.maxidev.wallsplash.common.utils.Constants.BASE_URL
import com.maxidev.wallsplash.common.utils.Constants.CACHE_CONTROL
import com.maxidev.wallsplash.common.utils.Constants.CACHE_MAX_AGE
import com.maxidev.wallsplash.common.utils.Constants.CLIENT_ID
import com.maxidev.wallsplash.common.utils.Constants.CONTENT_TYPE
import com.maxidev.wallsplash.common.utils.Constants.FILE_CACHE_NAME
import com.maxidev.wallsplash.common.utils.Constants.TIMER_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        json: Json,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(CONTENT_TYPE.toMediaType())
            )
            .build()
    }

    @Provides
    @Singleton
    fun providesJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    @Provides
    @Singleton
    fun providesInterceptor(): Interceptor {
        return Interceptor { chain ->
            val cacheControl = CacheControl.Builder()
                .maxAge(maxAge = CACHE_MAX_AGE, timeUnit = TimeUnit.HOURS)
                .build()
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader(AUTHORIZATION, "$CLIENT_ID $API_KEY")
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()

            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun providesCache(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): OkHttpClient {
        val file = File(context.cacheDir, FILE_CACHE_NAME)
        val cache = Cache(file, 10 * 1024 * 1024)

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(interceptor)
            .connectTimeout(TIMER_OUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): WallsplashApiService =
        retrofit.create(WallsplashApiService::class.java)
}