package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.OtpResponse
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepository(
    private val apiService: ApiService,
    private val userManager: UserManager,
    private val deviceID: DeviceID,
) {

    suspend fun getCardUsageHistory(creditCard: CreditCard): Result<List<CardUsageHistory>> {
        return withContext(Dispatchers.IO) {
            try {
                val cardUsageHistoryResponse =
                    apiService.getCardUsageHistory(
                        RequestHelper.createCardUsageHistory(
                            userManager.memberNumber!!,
                            creditCard,
                            deviceId = deviceID.deviceId
                        )
                    )
                Result.Success(cardUsageHistoryResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("RemoteRepository...getCardUsageHistory has error $e")
                Result.Error(e)
            }
        }
    }

    suspend fun getCardUsageHistoryWithoutMember(creditCard: CreditCard): Result<List<CardUsageHistory>> {
        return withContext(Dispatchers.IO) {
            try {
                val cardUsageHistoryResponse =
                    apiService.getCardUsageHistory(
                        RequestHelper.createCardUsageHistoryWithouMember(
                            creditCard,
                            deviceId = deviceID.deviceId
                        )
                    )
                Result.Success(cardUsageHistoryResponse.response.cardInfo!!)
            } catch (e: Exception) {
                println("RemoteRepository...getCardUsageHistory has error $e")
                Result.Error(e)
            }
        }
    }


    suspend fun requestWebDirectOtp(): Result<OtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                //fake
                val otpResponse = OtpResponse(otp = "123456")
                Result.Success(otpResponse)
            } catch (e: Exception) {
                println("RemoteRepository...registerBiometric has error $e")
                Result.Error(e)
            }
        }
    }
}