package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.model.*
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("LoginReq")
    suspend fun login(
        @Body loginRequest: Request
    ): LoginResponse

    @POST("MemberSelReq")
    suspend fun getUser(
        @Body memberSelectRequest: Request
    ): MemberResponse

    @POST("CardSelReq")
    suspend fun getCard(
        @Body cardRequest: Request
    ): CardInfoResponse

    @POST("IssueGiftReq")
    suspend fun issueGiftCard(
        @Body cardRequest: Request
    ): IssueGiftReqResponse

    @POST("GiftNumberAuthReq")
    suspend fun getGiftInfo(
        @Body giftNumberAuthRequest: Request
    ): GiftNumberAuthReqResponse

    @POST("FeeSelReq")
    suspend fun getFeeSel(
        @Body feeSelReqRequest: Request
    ): FeeSelReqResponse

    @POST("IssueSumReq")
    suspend fun issueSumReq(
        @Body issueSumReqResponse: Request
    ): IssueSumReqResponse

    @POST("CardListSelReq")
    suspend fun getListCards(
        @Body cardListRequest: Request
    ): CardResponse

    @POST("CardDesignSelReq")
    suspend fun getListDesign(
        @Body listDesignRequest: Request
    ): ListDesignResponse

    @POST("MemberUpdReq")
    suspend fun changeInfoMember(
        @Body changeInfoMemberRequest: Request
    ): ChangeInfoMemberResponse

    @POST("SuspendDealSelReq")
    suspend fun getListSuspendDeal(
        @Body suspendListRequest: Request
    ): SuspendDealResponse

    @POST("CardDealHisReq")
    suspend fun getCardUsageHistory(
        @Body cardListRequest: Request
    ): CardUsageHistoryResponse

    @POST("CardUpdReq")
    suspend fun updateCard(
        @Body updateCardRequest: Request
    ): UpdateCardResponse

    @POST("PasswordUpdReq")
    suspend fun updatePassword(
        @Body updatePasswordRequest: Request
    ): PasswordUpdateResponse

    @POST("PasswordResetReq")
    suspend fun resetPassword(
        @Body resetPasswordRequest: Request
    ): PasswordResetResponse

    @POST("CardRepublishReq")
    suspend fun republishCard(
        @Body cardRepublishRequest: Request
    ): UpdateCardResponse

    @POST("CardCertifiNoMemReq")
    suspend fun certifiGift(
        @Body giftCardCertifiRequest: Request
    ): GiftCertifiResponse

    @POST("CardRelationRegReq")
    suspend fun cardRelation(
        @Body cardRelationRequest: Request
    ): CardRelationRegReqResponse

    @POST("SmsIvrAuthCodeSendReq")
    suspend fun sendSMSRequest(
        @Body sendSMSRequest: Request
    ): SendSMSResponse

    @POST("SmsAuthCodeSendReq")
    suspend fun sendSmsRequest(
        @Body sendSmsRequest: Request
    ): SMSAuthCodeSendResponse

    @POST("SmsAuthReq")
    suspend fun confirmSMS(
        @Body sendSmsRequest: Request
    ): SMSAuthResponse

    @FormUrlEncoded
    @POST("OTPGetReq")
    suspend fun requestWebDirectOtp(
        @Header("Authorization") authorization: String
    ): OtpResponse
}