package com.example.discogsapp.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.discogs.com/"

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

private fun authorizationInterceptor(authorization: String): Interceptor = Interceptor { chain ->
    val request = chain.request()
        .newBuilder()
        .addHeader("Authorization", authorization)
        .build()

    chain.proceed(request)
}

fun retrofitAPI(
    authorization: String,
    isDebug: Boolean = false,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(authorizationInterceptor(authorization))
                .addInterceptor(loggingInterceptor(isDebug))
                .build(),
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
