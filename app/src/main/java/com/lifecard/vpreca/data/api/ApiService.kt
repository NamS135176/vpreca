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

    @POST("IssueGiftReq")
    suspend fun issueGiftCard(
        @Body cardRequest: BrandRequest
    ): IssueGiftReqResponse

    @POST("GiftNumberAuthReq")
    suspend fun getGiftInfo(
        @Body giftNumberAuthRequest: BrandRequest
    ): GiftNumberAuthReqResponse

    @POST("FeeSelReq")
    suspend fun getFeeSel(
        @Body feeSelReqRequest: BrandRequest
    ): FeeSelReqResponse

    @POST("IssueSumReq")
    suspend fun issueSumReq(
        @Body issueSumReqResponse: BrandRequest
    ): IssueSumReqResponse

    @POST("CardListSelReq")
    suspend fun getListCards(
        @Body cardListRequest: BrandRequest
    ): CardResponse

    @POST("CardDesignSelReq")
    suspend fun getListDesign(
        @Body listDesignRequest: BrandRequest
    ): ListDesignResponse

    @POST("MemberUpdReq")
    suspend fun changeInfoMember(
        @Body changeInfoMemberRequest: BrandRequest
    ): ChangeInfoMemberResponse

    @POST("SuspendDealSelReq")
    suspend fun getListSuspendDeal(
        @Body suspendListRequest: BrandRequest
    ): SuspendDealResponse

    @POST("CardDealHisReq")
    suspend fun getCardUsageHistory(
        @Body cardListRequest: BrandRequest
    ): CardUsageHistoryResponse

    @POST("CardUpdReq")
    suspend fun updateCard(
        @Body updateCardRequest: BrandRequest
    ): UpdateCardResponse

    @POST("PasswordUpdReq")
    suspend fun updatePassword(
        @Body updatePasswordRequest: BrandRequest
    ): PasswordUpdateResponse

    @POST("CardRepublishReq")
    suspend fun republishCard(
        @Body cardRepublishRequest: BrandRequest
    ): UpdateCardResponse

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