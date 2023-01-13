package com.tinude.newsheadlines.network

import com.google.gson.Gson
import com.tinude.newsheadlines.util.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is IOException -> ResultWrapper.GenericError(103, Constants.ERROR.NO_CONNECTION)
                is UnknownHostException -> ResultWrapper.GenericError(
                    101,
                    Constants.ERROR.NO_CONNECTION
                )
                is SocketTimeoutException -> ResultWrapper.GenericError(
                    102,
                    Constants.ERROR.NO_CONNECTION
                )
                is HttpException -> {
                    when (throwable.code()) {
                        422 -> ResultWrapper.GenericError(
                            throwable.code(),
                            parseErrorBody()(throwable)
                        )
                        else -> ResultWrapper.GenericError(throwable.code(), "Something went wrong")
                    }
                }
                else -> {
                    ResultWrapper.GenericError(null, throwable.localizedMessage ?: "Error")
                }
            }
        }
    }
}

fun parseErrorBody(): (Throwable) -> String {
    return { throwable ->
        when {
            throwable is HttpException -> {
                try {
                    val responseMap = Gson().fromJson(
                        throwable.response()?.errorBody()!!.string(),
                        HashMap::class.java
                    ) as HashMap<*, *>
                    "${(responseMap["message"])}"
                } catch (e: Exception) {
                    "genericResponse"
                }
            }
            else -> throwable.message ?: ""
        }
    }
}
