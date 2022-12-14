package com.lifecard.vpreca.data

import android.content.Context
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.utils.RequestHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class UserRepository(
    private val appContext: Context,
    private val apiService: ApiService,
    private val userManager: UserManager,
    private val deviceID: DeviceID,
) {
    suspend fun login(loginRequest: Request): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(loginRequest)
                userManager.setLoggedIn(appContext, loginResponse)
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
                        RequestHelper.createMemberRequest(
                            loginId,
                            memberNumber,
                            deviceId = deviceID.deviceId
                        )
                    )
                    userManager.setLoggedMember(appContext, userResponse.response)
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
    ): Result<MemberInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.changeInfoMember(
                    RequestHelper.createChangeInfoMember(memberInfo, deviceId = deviceID.deviceId)
                )
                userManager.setLoggedMember(appContext, userResponse.response)
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
                        deviceId = deviceID.deviceId,
                        memberInfo = PasswordUpdateMemberInfoContent(
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
        birthday: String?,
        phone: String?,
        secretQuestion: String,
        secretAnswer: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.resetPassword(
                    RequestHelper.createResetPassRequest(
                        deviceId = deviceID.deviceId,
                        memberInfo = PasswordResetMemberInfoContent(
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

    suspend fun sendSMSRequest(
        loginId: String?
    ): Result<SendSMSResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.sendSMSRequest(
                    RequestHelper.createSendSMSRequest(
                        loginId = loginId!!,
                        deviceId = deviceID.deviceId
                    )
                )
                Result.Success(userResponse.response)
            } catch (e: Exception) {
                println("UserRepository... request sms has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun sendSMSConfirm(
        memberNumber: String,
        certType: String,
        operationType: String,
        certCode: String,
        extCertDealId: String
    ): Result<SMSAuthResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.confirmSMS(
                    RequestHelper.createSMSConfirm(
                        memberNumber = memberNumber,
                        certType = certType,
                        operationType = operationType,
                        certCode = certCode,
                        extCertDealId = extCertDealId,
                        deviceId = deviceID.deviceId
                    )
                )

                Result.Success(userResponse.response)
            } catch (e: Exception) {
                println("UserRepository... request sms has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }

    suspend fun sendSMSIvrConfirm(
        certType: String,
        loginId: String,
        certCode: String,
        extCertDealId: String
    ): Result<SmsIvrAuthReqResponseContent> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = apiService.confirmSMSIvr(
                    RequestHelper.createSMSIvrConfirm(
                        certType = certType,
                        loginId = loginId,
                        certCode = certCode,
                        extCertDealId = extCertDealId,
                        deviceId = deviceID.deviceId
                    )
                )

                Result.Success(userResponse.response)
            } catch (e: Exception) {
                println("UserRepository... request sms has error $e")
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}