package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class UserRepository(private val apiService: ApiService, private val userManager: UserManager) {
    suspend fun login(loginRequest: BrandRequest): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(loginRequest)
                userManager.setLoggedIn(loginResponse)
                Result.Success(loginResponse)
            } catch (e: Exception) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun loginWithBiometric(username: String, signed: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.loginWithBiometric(username, response = signed)
                userManager.setLoggedIn(loginResponse)
                Result.Success(loginResponse)
            } catch (e: Exception) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun getUser(
        loginId: String? = userManager.memberInfo?.loginId,
        memberNumber: String? = userManager.memberInfo?.memberNumber
    ): Result<MemberInfo> {
        return withContext(Dispatchers.IO) {
            try {
                if (loginId == null || memberNumber == null) {
                    Result.Error(IOException("Can not get user"))
                } else {
                    val userResponse = apiService.getUser(
                        RequestHelper.createMemberRequest(loginId, memberNumber)
                    )
                    userManager.setLoggedMember(userResponse.brandPrecaApi.response)
                    Result.Success(userResponse.brandPrecaApi.response.memberInfo)
                }
            } catch (e: Exception) {
                println("UserRepository... getUser has error $e")
                e.printStackTrace()
                Result.Error(IOException("Can not get user", e))
            }
        }
    }
}