package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CreditCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class CreditCardRepository(
    private val apiService: ApiService,
    private val userRepository: UserRepository
) {
    // Mutex to make writes to cached values thread-safe.
    private val latestCardsMutex = Mutex()

    private var latestCards: List<CreditCard> = emptyList()

    suspend fun getLatestCards(refresh: Boolean = false): Result<List<CreditCard>> {
        return withContext(Dispatchers.IO) {
            if (refresh || latestCards.isEmpty()) {
                try {
                    val response =
                        apiService.getListCards(authorization = "Bear ${userRepository.accessToken!!}")
                    latestCardsMutex.withLock { latestCards = response.data }
                    latestCardsMutex.withLock { Result.Success(latestCards) }
                } catch (e: Exception) {
                    print("CreditCardRepository getLatestCards has error ${e}")
                    Result.Error(e)
                }
            } else {
                latestCardsMutex.withLock { Result.Success(latestCards) }
            }
        }
    }
}