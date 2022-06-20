package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.SuspendDeal
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SuspendDealRepository(
    private val apiService: ApiService,
    private val userManager: UserManager
) {
    suspend fun getListSuspendDeal(): Result<List<SuspendDeal>> {
        return withContext(Dispatchers.IO) {
            try {
                val suspendDealResponse = apiService.getListSuspendDeal(
                    RequestHelper.createSuspendDealListRequest(memberNumber = userManager.memberNumber!!)
                )
                Result.Success(suspendDealResponse.response.suspendDeal!!)
            } catch (e: Exception) {
                println("SuspendDealRepository... getListSuspendDeal has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}