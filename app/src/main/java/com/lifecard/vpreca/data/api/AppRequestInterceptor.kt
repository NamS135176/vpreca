package com.lifecard.vpreca.data.api

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.lifecard.vpreca.MainActivity
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.model.BaseResponse
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.ApiError
import com.lifecard.vpreca.utils.bodyToString
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import okio.BufferedSource
import java.io.IOException
import java.net.UnknownHostException
import java.nio.charset.Charset


class AppRequestInterceptor(
    private var appContext: Context,
    private val userManager: UserManager
) : Interceptor {
    val gson = Gson()
    private val messageDigest = MessageDigest()
    private val lock = Mutex()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val newRequestBuilder = request.newBuilder()

            userManager.accessToken?.let { accessToken ->
                newRequestBuilder.addHeader("accesstoken", accessToken)
            }

            //signature json body
            request.bodyToString()?.let { jsonBodyString ->
                val signature = messageDigest.sign(jsonBodyString)
                signature?.let {
                    newRequestBuilder.addHeader("signature", it)
                }
            }

            return handleResponse(chain, chain.proceed(newRequestBuilder.build()))
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

    private fun handleResponse(
        chain: Interceptor.Chain,
        response: Response,
        checkTokenExpire: Boolean = true
    ): Response {
        val code = response.code()
        if (code == 500) {
            throw InternalServerException()
        } else if (code > 400) {
            throw ApiException(
                resultCode = ApiError.otherErrorCode,
                messageType = ApiError.otherMessageType,
                ApiError.otherErrorMessage
            )
        } else if (code == 200) {
            return handleSuccess200(chain, response, checkTokenExpire = checkTokenExpire)
        }
        return response
    }

    private fun handleSuccess200(
        chain: Interceptor.Chain,
        response: Response,
        checkTokenExpire: Boolean = true
    ): Response {
        return response.body()?.let { responseBody ->
            response.header("accesstoken")
                ?.let { accessToken -> userManager.saveAccessToken(accessToken) }
            response.header("refreshtoken")
                ?.let { refreshToken -> userManager.saveRefreshToken(refreshToken) }

            val source: BufferedSource = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.

            val buffer = source.buffer()
            val bodyText = buffer.clone().readString(Charset.forName("UTF-8"))
            try {
                val json: BaseResponse = gson.fromJson(
                    bodyText,
                    BaseResponse::class.java
                )
                val resultCode = json.response.resultCode
                if (ApiError.isResultCodeRefreshTokenInValid(resultCode)) {
                    goToLoginScreen()
                } else if (ApiError.isResultCodeAccessTokenExpire(resultCode) && checkTokenExpire) {
                    return@let handleAccessTokenExpired(chain)
                } else if (ApiError.isResultCodeError(resultCode)) {
                    val messageType = json.head.messageType
                    throw ApiException.createApiException(
                        resultCode = resultCode,
                        messageType = messageType
                    )
                }
            } catch (e: JsonSyntaxException) {
            } catch (e: NullPointerException) {
            }
            return@let response
        } ?: kotlin.run { response }
    }

    private fun handleAccessTokenExpired(chain: Interceptor.Chain): Response? {
        return runBlocking {
            lock.withLock {
                if (userManager.accessToken.isNullOrEmpty()
                    || userManager.refreshToken.isNullOrEmpty()
                ) {
                    val request = chain.request().newBuilder()
                        .addHeader("accesstoken", userManager.accessToken!!)
                        .addHeader("refreshtoken", userManager.refreshToken!!)
                        .build()
                    return@runBlocking handleResponse(
                        chain,
                        chain.proceed(request),
                        checkTokenExpire = false//only check fist time
                    )
                } else {
                    goToLoginScreen()
                    return@runBlocking null
                }
            }
        }

    }

    private fun goToLoginScreen() {
        //go to login screen
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.EXTRA_FORCE_SHOW_LOGIN, true)
        }
        appContext.startActivity(intent)
    }
}