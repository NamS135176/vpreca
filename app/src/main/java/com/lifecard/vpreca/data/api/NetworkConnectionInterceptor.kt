package com.lifecard.vpreca.data.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.lifecard.vpreca.data.model.BaseResponse
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.ApiError
import com.lifecard.vpreca.utils.getMessageType
import okhttp3.Interceptor
import okhttp3.Response
import okio.BufferedSource
import java.io.IOException
import java.net.UnknownHostException
import java.nio.charset.Charset


class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            val code = response.code()
            if (code == 500) {
                throw InternalServerException()
            } else if (code > 400) {
                val messageType = request.body().getMessageType()
                throw ApiException(
                    resultCode = ApiError.otherErrorCode,
                    messageType = messageType,
                    ApiError.otherErrorMessage
                )
            } else if (code == 200) {
                //check result code
                handleSuccess200(response)
            }
            return response
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            throw NoConnectivityException()
        } catch (e: ApiException) {
            throw e
        } catch (e: IOException) {
            e.printStackTrace()
            throw NoConnectivityException()
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun handleSuccess200(response: Response) {
        response.body()?.let { responseBody ->
            val source: BufferedSource = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.

            val buffer = source.buffer()
            val bodyText = buffer.clone().readString(Charset.forName("UTF-8"))

            /*
            val headers = response.headers()
            if ("gzip".equals(headers.get("Content-Encoding"), ignoreCase = true)) {
                var gzippedResponseBody: GzipSource? = null
                try {
                    gzippedResponseBody = GzipSource(buffer.clone())
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                } finally {
                    gzippedResponseBody?.close()
                }
            }
            */
            try {
                val json: BaseResponse = gson.fromJson(
                    bodyText,
                    BaseResponse::class.java
                )
                val resultCode = json.response.resultCode
                if (ApiError.isResultCodeError(resultCode)) {
                    val messageType = json.head.messageType
                    throw ApiException.createApiException(
                        resultCode = resultCode,
                        messageType = messageType
                    )
                }
            } catch (e: JsonSyntaxException) {
            } catch (e: NullPointerException) {
            }
        }
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

}