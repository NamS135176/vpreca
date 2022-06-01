package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.*
import retrofit2.http.*

interface ApiService {
    @POST("LoginReq")
    suspend fun login(
        @Body loginRequest: BrandRequest
    ): LoginResponse

    @POST("MemberSelReq")
    suspend fun getUser(
        @Body memberSelectRequest: BrandRequest
    ): MemberResponse

    @POST("CardSelReq")
    suspend fun getCard(
        @Body cardRequest: BrandRequest
    ): CardInfoResponse

    @POST("CardListSelReq")
    suspend fun getListCards(
        @Body cardListRequest: BrandRequest
    ): CardResponse

    @POST("SuspendDealSelReq")
    suspend fun getListSuspendDeal(
        @Body suspendListRequest: BrandRequest
    ): SuspendDealResponse

    @POST("CardDealHisReq")
    suspend fun getCardUsageHistory(
        @Body cardListRequest: BrandRequest
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

    @FormUrlEncoded
    @POST("otp")
    suspend fun requestWebDirectOtp(
        @Header("Authorization") authorization: String
    ): OtpResponse
}