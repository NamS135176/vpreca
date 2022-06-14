package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class UserRepository(private val apiService: ApiService, private val userManager: UserManager) {
    suspend fun login(loginRequest: Request): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(loginRequest)
                userManager.setLoggedIn(loginResponse)
                Result.Success(loginResponse)
            } catch (e: Exception) {
                println("LoginDataSource... login has error $e")
                e.printStackTrace()
                Result.Error(e)
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
                println("LoginDataSource... login has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun getUser(
        loginId: String? = userManager.loginId,
        memberNumber: String? = userManager.memberNumber
    ): Result<MemberInfo> {
        return withContext(Dispatchers.IO) {
            try {
                if (loginId == null || memberNumber == null) {
                    Result.Error(IOException("Can not get user"))
                } else {
                    val userResponse = apiService.getUser(
                        RequestHelper.createMemberRequest(loginId, memberNumber)
                    )
                    userManager.setLoggedMember(userResponse.response)
                    Result.Success(userResponse.response.memberInfo!!)
                }
            } catch (e: Exception) {
                println("UserRepository... getUser has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun changeInfoMember(
        memberInfo: ChangeInfoMemberData
    ): Result<ChangeInfoMemberData> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.changeInfoMember(
                    RequestHelper.createChangeInfoMember(memberInfo)
                )

                Result.Success(userResponse.response.memberInfo!!)
            } catch (e: Exception) {
                println("UserRepository... change info has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun updatePassword(
        currentPass: String,
        newPass: String
    ): Result<MemberInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.updatePassword(
                    RequestHelper.createChangePassRequest(
                        PasswordUpdateMemberInfoContent(
                            userManager.loginId!!,
                            userManager.memberNumber!!,
                            "1",
                            currentPass,
                            newPass
                        )
                    )
                )

                Result.Success(userResponse.response.memberInfo!!)
            } catch (e: Exception) {
                println("UserRepository... change pass has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun resetPassword(
        email: String,
        birthday: String,
        phone: String,
        secretQuestion: String,
        secretAnswer: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.resetPassword(
                    RequestHelper.createResetPassRequest(
                        PasswordResetMemberInfoContent(
                            email,
                            birthday,
                            phone,
                            secretQuestion,
                            secretAnswer
                        )
                    )
                )

                Result.Success(userResponse.response.resultCode)
            } catch (e: Exception) {
                println("UserRepository... reset pass has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}