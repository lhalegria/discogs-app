package com.example.discogsapp.data.extension

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException

private val moshi by lazy { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }

fun <T> Flow<T>.parseHttpError(): Flow<T> =
    catch { throwable ->
        throw when (throwable) {
            is HttpException -> {
                try {
                    val httpCode = throwable.code()
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val response = moshi.adapter(ErrorBodyResponse::class.java).fromJson(errorBody.orEmpty())
                    val message = response?.message.orEmpty()

                    ErrorBodyException(httpCode, message, throwable)
                } catch (exception: Exception) {
                    ErrorBodyParseException(exception)
                }
            }

            else -> throwable
        }
    }

class ErrorBodyException(
    val httpCode: Int,
    override val message: String,
    cause: Throwable,
) : Exception(message, cause)

class ErrorBodyParseException(cause: Throwable) : Exception(cause)

internal data class ErrorBodyResponse(
    @Json(name = "http_status_code") val statusCode: Int,
    @Json(name = "message") val message: String,
    @Json(name = "code") val code: String,
)
