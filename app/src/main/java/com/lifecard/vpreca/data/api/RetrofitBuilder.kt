package com.lifecard.vpreca.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "http://192.168.1.3:3001"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().run {
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

}