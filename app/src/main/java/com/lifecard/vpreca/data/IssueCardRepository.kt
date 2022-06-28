package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class IssueCardRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {
    suspend fun giftNumberAuthReq(
        giftNumber: String
    ): Result<GiftInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val giftNumberAuthResponse = apiService.getGiftInfo(
                    RequestHelper.createGiftNumberAuthReqRequest(
                        memberNumber = userManager.memberNumber!!,
                        giftNumber
                    )
                )
                Result.Success(giftNumberAuthResponse.response.giftNumberInfo!!)
            } catch (e: Exception) {
                println("IssueCardRepository... giftNumberAuthReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun issueSumReq(
        cardInfo: CardInfoWithDesignIdContentInfo,
        sumUpSrcCardInfo: ArrayList<CardInfoRequestContentInfo>,
        cardSchemeId: String,
        feeType: String,
        targetAmount: String,
        sumCount: String
    ): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiService.getFeeSel(
                    RequestHelper.createFeeSelReqRequest(cardSchemeId, feeType, targetAmount)
                )

                val sumUpInfo = SumUpInfoContentInfo(
                    res.response.feeInfo?.get(0)?.feeAmount!!,
                    res.response.feeInfo.get(0).feeGetResultType,
                    res.response.feeInfo.get(0).feeCalculateType,
                    res.response.feeInfo.get(0).feeInclusiveFlg,
                    sumCount
                )
                val feeSelReqResponse = apiService.issueSumReq(
                    RequestHelper.createIssueSumRequest(
                        memberNumber = userManager.memberNumber!!,
                        cardInfo,
                        sumUpInfo,
                        sumUpSrcCardInfo
                    )
                )
                Result.Success(feeSelReqResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("IssueCardRepository... issueSumReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun issueSumReqBalance(
        sumUpSrcCardInfo: ArrayList<CardInfoRequestContentInfo>,
        cardSchemeId: String,
        feeType: String,
        targetAmount: String,
        sumCount: String,
        designId: String,
        giftNumber: String,
        cardNickname: String,
        vcnName: String
    ): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {

                val r = apiService.issueGiftCard(
                    RequestHelper.createIssueGiftRequestWithCard(
                        memberNumber = userManager.memberNumber!!,
                        CardInfoWithCard(cardSchemeId, designId, cardNickname, vcnName),
                        giftNumber
                    )
                )

                val res = apiService.getFeeSel(
                    RequestHelper.createFeeSelReqRequest(r.response.cardInfo!!.cardSchemeId, feeType, targetAmount)
                )

                val sumUpInfo = SumUpInfoContentInfo(
                    res.response.feeInfo?.get(0)?.feeAmount!!,
                    res.response.feeInfo.get(0).feeGetResultType,
                    res.response.feeInfo.get(0).feeCalculateType,
                    res.response.feeInfo.get(0).feeInclusiveFlg,
                    sumCount
                )
                val feeSelReqResponse = apiService.issueSumReq(
                    RequestHelper.createIssueSumRequest(
                        memberNumber = userManager.memberNumber!!,
                        CardInfoWithDesignIdContentInfo(
                            r.response.cardInfo!!.cardSchemeId,
                            r.response.cardInfo.designId,
                            r.response.cardInfo.cardNickname,
                            r.response.cardInfo.vcnName
                        ),
                        sumUpInfo,
                        sumUpSrcCardInfo
                    )
                )
                Result.Success(feeSelReqResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("IssueCardRepository... issueSumReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }


    suspend fun issueGiftReqWithouCard(
        designId: String,
        giftNumber: String
    ): Result<IssueGiftReqResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiService.issueGiftCard(
                    RequestHelper.createIssueGiftRequestWithoutCard(
                        memberNumber = userManager.memberNumber!!,
                        CardInfoOnlyDesignId(designId),
                        giftNumber
                    )
                )
                Result.Success(res.response)
            } catch (e: Exception) {
                println("IssueCardRepository... issueSumReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun issueGiftReqWithCard(
        designId: String,
        giftNumber: String,
        cardSchemeId: String,
        cardNickname: String,
        vcnName: String
    ): Result<IssueGiftReqResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiService.issueGiftCard(
                    RequestHelper.createIssueGiftRequestWithCard(
                        memberNumber = userManager.memberNumber!!,
                        CardInfoWithCard(cardSchemeId, designId, cardNickname, vcnName),
                        giftNumber
                    )
                )
                Result.Success(res.response)
            } catch (e: Exception) {
                println("IssueCardRepository... issueSumReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}