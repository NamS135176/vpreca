package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
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

    private var latestCards: List<CreditCard>? = emptyList()

    suspend fun getLatestCards(refresh: Boolean = false): Result<List<CreditCard>> {
        return withContext(Dispatchers.IO) {
            if (refresh || latestCards?.isEmpty()!!) {
                try {
                    val response =
                        apiService.getListCards(RequestHelper.createCardListRequest(memberNumber = userManager.memberNumber!!))
                    latestCardsMutex.withLock {
                        latestCards = response.response.cardInfo
                    }
                    latestCardsMutex.withLock { Result.Success(latestCards!!) }
                } catch (e: Exception) {
                    println("CreditCardRepository getLatestCards has error $e")
                    e.printStackTrace()
                    Result.Error(e)
                }
            } else {
                latestCardsMutex.withLock { Result.Success(latestCards!!) }
            }
        }
    }

    fun latestCardEmpty(): Boolean {
        return latestCards?.isEmpty()!!
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
                Result.Success(cardResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("CreditCardRepository... getCard has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun updateCard(creditCard: CreditCard): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {
                val updateCardResponse = apiService.updateCard(
                    RequestHelper.createUpdateCardRequest(
                        memberNumber = userManager.memberNumber!!,
                        creditCard
                    )
                )
                Result.Success(updateCardResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("CreditCardRepository... update card has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun republishCard(creditCard: CreditCard): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {
                val republishCardResponse = apiService.republishCard(
                    RequestHelper.createRepublishCardRequest(
                        memberNumber = userManager.memberNumber!!,
                        creditCard.cardSchemeId,
                        creditCard.precaNumber,
                        creditCard.vcn,
                        creditCard.cooperatorNumber
                    )
                )
                Result.Success(republishCardResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("CreditCardRepository... republish card has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun getListDesign(cardSchemeId: String): Result<ListDesignResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val republishCardResponse = apiService.getListDesign(
                    RequestHelper.createListDesignRequest(
                      cardSchemeId = cardSchemeId
                    )
                )
                Result.Success(republishCardResponse.response)
            } catch (e: Exception) {
                println("CreditCardRepository... republish card has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun giftCardInfo(confirmationNumber: String, vcnFourDigits:String): Result<CardInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val republishCardResponse = apiService.certifiGift(
                    RequestHelper.createGiftCertifiRequest(
                       GiftCertifiCardInfoRequestContentInfo(confirmationNumber, vcnFourDigits)
                    )
                )
                val cardResponse = apiService.getCard(
                    RequestHelper.createCardInfoWithouMemberRequest(
                        republishCardResponse.response.cardSchemeId!!,
                        republishCardResponse.response.precaNumber!!,
                        republishCardResponse.response.vcn!!,
                    )
                )
                Result.Success(cardResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("CreditCardRepository... giftCardInfo has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun cardRelation(vcn: String, vcnExpirationDate:String, vcnSecurityCode:String,cardNickname:String): Result<CreditCard> {
        return withContext(Dispatchers.IO) {
            try {
                val republishCardResponse = apiService.cardRelation(
                    RequestHelper.createCardRelationRequest(
                        memberNumber = userManager.memberNumber!!,
                        CardRelationRegRequestContentInfo(vcn, vcnExpirationDate, vcnSecurityCode, cardNickname)
                    )
                )
                Result.Success(republishCardResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("CreditCardRepository... cardRelation has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}