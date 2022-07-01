package com.lifecard.vpreca.data.api

import android.content.Context
import android.os.Build
import com.google.gson.GsonBuilder
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.utils.Constant
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceFactory {
    companion object {
        fun createService(
            appContext: Context,
            userManager: UserManager
        ): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(
                    AppRequestInterceptor(
                        appContext = appContext,
                        userManager = userManager
                    )
                )
                .apply {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        certificatePinner(
                            CertificatePinner.Builder()
                                .add(
                                    "execute-api.ap-southeast-1.amazonaws.com",
                                    "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
                                )
                                .build()
                        )
                    }
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }
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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    certificatePinner(
                        CertificatePinner.Builder()
                            .add(
                                "vision.googleapis.com",
                                "sha256/hxqRlPTu1bMS/0DITB1SSu0vd4u/8l8TjPgfaAp63Gc="
                            )
                            .build()
                    )
                }
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

        fun createAWSTextractService(): AWSTextractService {
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
                baseUrl(Constant.API_BASE_URL)
                addConverterFactory(GsonConverterFactory.create(gson))
                build()
            }
            return retrofit.create(AWSTextractService::class.java)
        }
    }
}