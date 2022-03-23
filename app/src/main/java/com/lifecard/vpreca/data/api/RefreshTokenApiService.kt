package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.CardResponse
import com.lifecard.vpreca.data.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RefreshTokenApiService {
    @FormUrlEncoded
    @POST("refresh")
    suspend fun refreshToken(
        @Field("access_token") accessToken: String,
        @Field("refresh_token") refreshToken: String
    ): LoginResponse
}