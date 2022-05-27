package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.BioChallenge
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.data.model.OtpResponse
import com.lifecard.vpreca.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RemoteRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {

    suspend fun getCardUsageHistory(): Result<List<CardUsageHistory>> {
        return withContext(Dispatchers.IO) {
            try {
                val cardUsageHistoryResponse =
                    apiService.getCardUsageHistory(userManager.bearAccessToken!!)
                Result.Success(cardUsageHistoryResponse.items)
            } catch (e: Exception) {
                println("RemoteRepository...getCardUsageHistory has error $e")
                Result.Error(IOException("Error getCardUsageHistory", e))
            }
        }
    }

    suspend fun getBioChallenge(username: String): Result<BioChallenge> {
        return withContext(Dispatchers.IO) {
            try {
                val bioChallenge =
                    apiService.getBioChallenge(memberNumber = username)
                Result.Success(bioChallenge)
            } catch (e: Exception) {
                println("RemoteRepository...getBioChallenge has error $e")
                Result.Error(IOException("Error getBioChallenge", e))
            }
        }
    }

    suspend fun registerBiometric(username: String, pemKey: String): Result<BioChallenge> {
        return withContext(Dispatchers.IO) {
            try {
                val bioChallenge =
                    apiService.registerBiometric(
                        userManager.bearAccessToken!!,
                        memberNumber = username,
                        bioKey = pemKey,
                        platform = "Android",
                        osVersion = android.os.Build.VERSION.SDK_INT.toString(),
                        algorithm = Constant.BIOMETRIC_ALGORITHM
                    )
                Result.Success(bioChallenge)
            } catch (e: Exception) {
                println("RemoteRepository...registerBiometric has error $e")
                Result.Error(IOException("Error registerBiometric", e))
            }
        }
    }

    suspend fun requestWebDirectOtp(): Result<OtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                //fake
                val otpResponse = OtpResponse(otp = "123456")
//                val otpResponse =
//                    apiService.requestWebDirectOtp(
//                        "Bear ${userRepository.accessToken!!}"
//                    )
                Result.Success(otpResponse)
            } catch (e: Exception) {
                println("RemoteRepository...registerBiometric has error $e")
                Result.Error(IOException("Error registerBiometric", e))
            }
        }
    }
}