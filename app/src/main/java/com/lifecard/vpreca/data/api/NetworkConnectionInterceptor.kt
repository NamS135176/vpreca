package com.lifecard.vpreca.data.api

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.startActivity
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class NetworkConnectionInterceptor(context: Context) : Interceptor {
    private val mContext: Context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code() > 400) {
            throw ApiException(statusCode = response.code())
        }
        return response
    }

    private val isConnected: Boolean
        get() {
            return true
            /*
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected

             */
        }

    init {
        mContext = context
    }
}