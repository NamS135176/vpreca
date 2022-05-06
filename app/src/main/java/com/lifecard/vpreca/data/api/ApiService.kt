package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.CardResponse
import com.lifecard.vpreca.data.model.CardUsageHistoryResponse
import com.lifecard.vpreca.data.model.LoginResponse
import com.lifecard.vpreca.data.model.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("cards")
    suspend fun getListCards(): CardResponse

    @POST("CardDealHisReq")
    suspend fun getCardUsageHistory(): CardUsageHistoryResponse
}