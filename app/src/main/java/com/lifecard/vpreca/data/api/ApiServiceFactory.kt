package com.lifecard.vpreca.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiServiceFactory {
    companion object {
        fun createService(
            appContext: Context,
            secureStore: SecureStore,
            userManager: UserManager
        ): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    userManager.bearAccessToken?.let { token ->
                        if (chain.request().header("Authorization").isNullOrEmpty()) {
                            val request =
                                chain.request().newBuilder()
                                    .addHeader("Authorization", token).build()
                            chain.proceed(request)
                        } else {
                            chain.proceed(chain.request())
                        }
                    } ?: kotlin.run {
                        chain.proceed(chain.request())
                    }
                })
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }
                .addInterceptor(NetworkConnectionInterceptor(appContext))
                .authenticator(TokenAuthenticator(appContext, secureStore))
                .build()
            val gson = GsonBuilder()
                .setDateFormat("yyyyMMddHHmmss")
                .create()
            val retrofit = Retrofit.Builder().run {
                client(client)
                baseUrl(Constant.API_BASE_URL)
                addConverterFactory(GsonConverterFactory.create(gson))
                build()
            }
            return retrofit.create(ApiService::class.java)
        }

        fun createGoogleVisionService(): GoogleVisionService {
            val client = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()
            val gson = GsonBuilder().create()
            val retrofit = Retrofit.Builder().run {
                client(client)
                baseUrl(Constant.GOOGLE_VISION_API)
                addConverterFactory(GsonConverterFactory.create(gson))
                build()
            }
            return retrofit.create(GoogleVisionService::class.java)
        }
    }
}