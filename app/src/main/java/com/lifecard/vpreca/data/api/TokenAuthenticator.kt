package com.lifecard.vpreca.data.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lifecard.vpreca.MainActivity
import com.lifecard.vpreca.data.model.LoginResponse
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.Constanst
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(private var appContext: Context, private val secureStore: SecureStore) :
    Authenticator {
    private var blacklistRefreshTokenUrlPath =
        arrayOf(
            "/login",
            "/sign-up",
            "/refresh",
            "/refresh-token",
            "/biometric-authentication",
            "/biometric",
            "/challenge"
        )
    private val lock = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            lock.withLock {
                val urlPath = response.request().url().encodedPath()
                var isBlocked = false
                blacklistRefreshTokenUrlPath.forEach { s ->
                    if (urlPath.indexOf(s) > 0) {
                        isBlocked = true
                    }
                }
                println("TokenAuthenticator... authenticate urlPath = $urlPath - isBlocked = $isBlocked")
                if (!isBlocked) {
                    val headerAccessToken: String? = response.request().header("Authorization")
                    val accessToken = secureStore.getAccessToken()
                    val refreshToken = secureStore.getRefreshToken()
                    if (headerAccessToken != "Bear $accessToken" && !headerAccessToken.isNullOrEmpty()) {
                        //case another api has been update access token, so we just update header and request again
                        return@runBlocking response.request().newBuilder()
                            .header("Authorization", headerAccessToken)
                            .build()

                    }
                    if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                        try {
                            val loginResponse = refreshToken(accessToken, refreshToken)
                            secureStore.saveAccessToken(loginResponse.accessToken)
                            secureStore.saveRefreshToken(loginResponse.refreshToken)
                            return@runBlocking response.request().newBuilder()
                                .header("Authorization", "Bearer ${loginResponse.accessToken}")
                                .build()
                        } catch (err: Throwable) {
                            //go to login screen
                            println("TokenAuthenticator... authenticate err $err")
                            val intent = Intent(appContext, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                putExtra(MainActivity.EXTRA_FORCE_SHOW_LOGIN, true)
                            }
                            appContext.startActivity(intent)
                        }
                    } else {
                        //go to login screen
                        val intent = Intent(appContext, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra(MainActivity.EXTRA_FORCE_SHOW_LOGIN, true)
                        }
                        appContext.startActivity(intent)
                    }
                }
                return@runBlocking null
            }
        }
    }

    private suspend fun refreshToken(accessToken: String, refreshToken: String): LoginResponse {
        val okHttpClient = OkHttpClient().newBuilder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constanst.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val service = retrofit.create(RefreshTokenApiService::class.java)
        return service.refreshToken(accessToken, refreshToken)
    }
}