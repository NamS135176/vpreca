package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.*
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
    suspend fun getCardUsageHistory(
        @Header("Authorization") authorization: String,
        @Field("memberNumber") memberNumber: String = "002",
    ): CardUsageHistoryResponse

    @GET("challenge")
    suspend fun getBioChallenge(@Query("loginId") memberNumber: String): BioChallenge

    @FormUrlEncoded
    @POST("biometric")
    suspend fun registerBiometric(
        @Header("Authorization") authorization: String,
        @Field("loginId") memberNumber: String,
        @Field("bioKey") bioKey: String,
        @Field("platform") platform: String,
        @Field("os_version") osVersion: String,
        @Field("algorithm") algorithm: String
    ): BioChallenge

    @FormUrlEncoded
    @POST("biometric-authentication")
    suspend fun loginWithBiometric(
        @Field("loginId") memberNumber: String,
        @Field("response") response: String
    ): LoginResponse
}