package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CardUsageHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RemoteRepository(private val apiService: ApiService) {

    suspend fun getCardUsageHistory(): Result<List<CardUsageHistory>> {
        return withContext(Dispatchers.IO) {
            try {
                var cardUsageHistoryResponse = apiService.getCardUsageHistory()
                Result.Success(cardUsageHistoryResponse.items)
            } catch (e: Throwable) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }
}