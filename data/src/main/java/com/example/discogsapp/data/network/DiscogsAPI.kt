package com.example.discogsapp.data.network

import com.example.discogsapp.data.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.discogs.com/"
private const val DEFAULT_USER_AGENT = "DiscogsApp/1.0 +https://github.com/lhalegria/discogs-app"
private val DEFAULT_AUTHORIZATION = when {
    BuildConfig.DISCOGS_API_KEY.isNotBlank() && BuildConfig.DISCOGS_API_SECRET.isNotBlank() -> {
        "Discogs key=%s, secret=%s".format(
            BuildConfig.DISCOGS_API_KEY,
            BuildConfig.DISCOGS_API_SECRET,
        )
    }

    else -> null
}

private val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private fun loggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        redactHeader("Authorization")
        level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

private fun headersInterceptor(): Interceptor = Interceptor { chain ->
    val requestBuilder = chain.request()
        .newBuilder()
        .addHeader("User-Agent", DEFAULT_USER_AGENT)

    DEFAULT_AUTHORIZATION?.let {
        requestBuilder.addHeader("Authorization", it)
    }

    val request = requestBuilder.build()

    chain.proceed(request)
}

fun retrofitAPI(): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(headersInterceptor())
                .addInterceptor(loggingInterceptor(BuildConfig.DEBUG))
                .build(),
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
