package com.lifecard.vpreca.data.api

import android.content.Context
import android.content.Intent
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.utils.Constanst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceFactory {
    companion object {
        fun createService(appContext: Context, secureStore: SecureStore) : ApiService {
            val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
                .authenticator(TokenAuthenticator(appContext, secureStore))
            .build()
            val retrofit = Retrofit.Builder().run {
                client(client)
                baseUrl(Constanst.API_BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                build()
            }
            return retrofit.create(ApiService::class.java)
        }
    }
}