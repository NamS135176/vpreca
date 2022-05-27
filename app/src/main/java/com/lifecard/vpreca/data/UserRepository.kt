package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.LoginAction
import com.lifecard.vpreca.data.model.LoginResponse
import com.lifecard.vpreca.data.model.User
import com.lifecard.vpreca.data.source.SecureStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class UserRepository(private val apiService: ApiService, private val userManager: UserManager) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(username, password)
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
        loginId: String? = userManager.user?.loginId,
        memberNumber: String? = userManager.user?.memberNumber
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                if (loginId == null || memberNumber == null) {
                    Result.Error(IOException("Can not get user"))
                } else {
                    userManager.bearAccessToken?.let { bearToken ->
                        val userResponse = apiService.getUser(
                            authorization = bearToken,
                            loginId = loginId!!,
                            memberNumber = memberNumber!!
                        )
                        userManager.setLoggedInUser(userResponse.user)
                        Result.Success(userResponse.user)
                    } ?: kotlin.run { Result.Error(IOException("Error logging in")) }
                }
            } catch (e: Exception) {
                println("UserRepository... getUser has error ${e}")
                Result.Error(IOException("Can not get user", e))
            }
        }
    }
}