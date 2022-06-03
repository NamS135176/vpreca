package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.utils.RequestHelper
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class IssueCardRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {
    suspend fun getFeeSelReq(
        cardSchemeId: String,
        feeType: String,
        targetAmount: String
    ): Result<List<FeeInfo>> {
        return withContext(Dispatchers.IO) {
            try {
                val feeSelReqResponse = apiService.getFeeSel(
                    RequestHelper.createFeeSelReqRequest(cardSchemeId, feeType, targetAmount)
                )
                Result.Success(feeSelReqResponse.brandPrecaApi.response.feeInfo)
            } catch (e: Exception) {
                println("IssueCardRepository... getFeeSelReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun issueSumReq(
        cardInfo: CardInfoWithDesignIdContentInfo,
        sumUpSrcCardInfo: CardInfoRequestContentInfo,
        cardSchemeId: String,
        feeType: String,
        targetAmount: String
    ): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {
                val res = apiService.getFeeSel(
                    RequestHelper.createFeeSelReqRequest(cardSchemeId, feeType, targetAmount)
                )
                val sumUpInfo = SumUpInfoContentInfo(
                    res.brandPrecaApi.response.feeInfo[0].feeAmount,
                    res.brandPrecaApi.response.feeInfo[0].feeGetResultType,
                    res.brandPrecaApi.response.feeInfo[0].feeCalculateType,
                    res.brandPrecaApi.response.feeInfo[0].feeInclusiveFlg,
                    "1"
                )
                val feeSelReqResponse = apiService.issueSumReq(
                    RequestHelper.createIssueSumRequest(
                        memberNumber = userManager.memberNumber!! ,
                        cardInfo,
                        sumUpInfo,
                        sumUpSrcCardInfo
                    )
                )
                Result.Success(feeSelReqResponse.brandPrecaApi.response.cardInfo)
            } catch (e: Exception) {
                println("IssueCardRepository... issueSumReq has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}