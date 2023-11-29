package com.github.radkoff26.nechto.extensions

import android.util.Log
import com.github.radkoff26.nechto.exceptions.LoadingException
import retrofit2.Response

fun <T> Response<T>.returnOrThrow(): T {
    if (isSuccessful) {
        return body()!!
    }
    Log.e("ResponseErrorTag", "returnOrThrow: ${code()}! ${message()}")
    throw LoadingException(code(), message())
}