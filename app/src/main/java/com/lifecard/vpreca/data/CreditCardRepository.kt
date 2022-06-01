package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.data.model.SuspendDeal
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException

class CreditCardRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {
    // Mutex to make writes to cached values thread-safe.
    private val latestCardsMutex = Mutex()

    private var latestCards: List<CreditCard> = emptyList()

    suspend fun getLatestCards(refresh: Boolean = false): Result<List<CreditCard>> {
        return withContext(Dispatchers.IO) {
            if (refresh || latestCards.isEmpty()) {
                try {
                    val response =
                        apiService.getListCards(RequestHelper.createCardListRequest(memberNumber = userManager.memberNumber!!))
                    latestCardsMutex.withLock {
                        latestCards = response.brandPrecaApi.response.cardInfo
                    }
                    latestCardsMutex.withLock { Result.Success(latestCards) }
                } catch (e: Exception) {
                    println("CreditCardRepository getLatestCards has error $e")
                    e.printStackTrace()
                    Result.Error(e)
                }
            } else {
                latestCardsMutex.withLock { Result.Success(latestCards) }
            }
        }
    }

    fun latestCardEmpty(): Boolean {
        return latestCards.isEmpty()
    }

    suspend fun getCard(
        cardSchemeId: String,
        precaNumber: String,
        vcn: String
    ): Result<CardInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val cardResponse = apiService.getCard(
                    RequestHelper.createCardInfoRequest(
                        memberNumber = userManager.memberNumber!!,
                        cardSchemeId,
                        precaNumber,
                        vcn
                    )
                )
                Result.Success(cardResponse.brandPrecaApi.response.cardInfo)
            } catch (e: Exception) {
                println("CreditCardRepository... getCard has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun getListSuspendDeal(): Result<List<SuspendDeal>> {
        return withContext(Dispatchers.IO) {
            try {
                val suspendDealResponse = apiService.getListSuspendDeal(
                    RequestHelper.createSuspendDealListRequest(memberNumber = userManager.memberNumber!!)
                )
                Result.Success(suspendDealResponse.brandPrecaApi.response.suspendDeal)
            } catch (e: Exception) {
                println("SuspendDealRepository... getListSuspendDeal has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}