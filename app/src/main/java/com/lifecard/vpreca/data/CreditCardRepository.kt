package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CreditCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.lang.Exception

class CreditCardRepository(private val apiService: ApiService) {
    // Mutex to make writes to cached values thread-safe.
    private val latestCardsMutex = Mutex()

    private var latestCards: List<CreditCard> = emptyList()

    suspend fun getLatestCards(refresh: Boolean = false): Result<List<CreditCard>> {
        return withContext(Dispatchers.IO) {
            if (refresh || latestCards.isEmpty()) {
                try {
                    val response = apiService.getListCards()
                    latestCardsMutex.withLock { latestCards = response.data }
                } catch (e: Throwable) {
                    Result.Error(Exception("Can not get card", e))
                }
            }
            latestCardsMutex.withLock { Result.Success(latestCards) }
        }
    }
}