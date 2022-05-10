package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.CardResponse
import com.lifecard.vpreca.data.model.CardUsageHistoryResponse
import com.lifecard.vpreca.data.model.LoginResponse
import com.lifecard.vpreca.data.model.User
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("loginId") username: String,
        @Field("loginPassword") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("CardListSelReq")
    suspend fun getListCards(
        @Header("Authorization") authorization: String,
        @Field("memberNumber") memberNumber: String = "002",
        @Field("invalidCardResFlg") invalidCardResFlg: String = "1"
    ): CardResponse

    @FormUrlEncoded
    @POST("CardDealHisReq")
    suspend fun getCardUsageHistory(@Header("Authorization") authorization: String): CardUsageHistoryResponse
}