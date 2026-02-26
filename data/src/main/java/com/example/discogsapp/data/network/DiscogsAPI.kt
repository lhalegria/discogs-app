package com.example.discogsapp.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.discogs.com/"

private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun authorizationInterceptor(authorization: String): Interceptor = Interceptor { chain ->
    val request = chain.request()
        .newBuilder()
        .addHeader("Authorization", authorization)
        .build()

    chain.proceed(request)
}

fun retrofitAPI(authorization: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(authorizationInterceptor(authorization))
                .addInterceptor(httpLoggingInterceptor)
                .build(),
        )
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
