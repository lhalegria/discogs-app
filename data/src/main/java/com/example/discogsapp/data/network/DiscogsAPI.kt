package com.example.discogsapp.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.discogs.com/"
private const val DEFAULT_USER_AGENT = "DiscogsApp/1.0 +https://github.com/lhalegria/discogs-app"

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

private fun headersInterceptor(
    authorization: String,
    userAgent: String,
): Interceptor = Interceptor { chain ->
    val request = chain.request()
        .newBuilder()
        .addHeader("Authorization", authorization)
        .addHeader("User-Agent", userAgent)
        .build()

    chain.proceed(request)
}

fun retrofitAPI(
    authorization: String,
    userAgent: String = DEFAULT_USER_AGENT,
    isDebug: Boolean = false,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(headersInterceptor(authorization, userAgent))
                .addInterceptor(loggingInterceptor(isDebug))
                .build(),
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
