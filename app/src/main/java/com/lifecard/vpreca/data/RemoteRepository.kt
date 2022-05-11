package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CardUsageHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RemoteRepository(
    private val apiService: ApiService,
    private val userRepository: UserRepository
) {

    suspend fun getCardUsageHistory(): Result<List<CardUsageHistory>> {
        return withContext(Dispatchers.IO) {
            try {
                val cardUsageHistoryResponse =
                    apiService.getCardUsageHistory("Bear ${userRepository.user?.accessToken!!}")
                Result.Success(cardUsageHistoryResponse.items)
            } catch (e: Exception) {
                println("RemoteRepository...getCardUsageHistory has error ${e}")
                Result.Error(IOException("Error getCardUsageHistory", e))
            }
        }
    }
}