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

class UserRepository(private val secureStore: SecureStore, private val apiService: ApiService) {
    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set
    var accessToken: String? = null
        private set
    var refreshToken: String? = null
        private set
    var loginAction: String? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        accessToken = secureStore.getAccessToken()
        refreshToken = secureStore.getRefreshToken()
        loginAction = secureStore.getLoginAction()

        println("UserRepository... init: accessToken=${accessToken}")
        if (accessToken != null && loginAction == LoginAction.None.value) {
            val fakeUser = User(
                "002",
                "The Anh",
                "anhndt@vn-sis.com",
            )
            user = fakeUser
        }
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(username, password)
                secureStore.saveAccessToken(loginResponse.accessToken)
                secureStore.saveRefreshToken(loginResponse.refreshToken)
                secureStore.saveLoginAction(loginResponse.action)
                secureStore.saveLoginUserId(username)
                accessToken = loginResponse.accessToken
                refreshToken = loginResponse.refreshToken
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
                secureStore.saveAccessToken(loginResponse.accessToken)
                secureStore.saveRefreshToken(loginResponse.refreshToken)
                secureStore.saveLoginAction(loginResponse.action)
                secureStore.saveLoginUserId(username)
                accessToken = loginResponse.accessToken
                refreshToken = loginResponse.refreshToken
                Result.Success(loginResponse)
            } catch (e: Exception) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun getUser(): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val fakeUser = User(
                    UUID.randomUUID().toString(),
                    "The Anh",
                    "anhndt@vn-sis.com",
                )
                setLoggedInUser(fakeUser)
                Result.Success(fakeUser)
            } catch (e: Exception) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        println("UserRepository... setLoggedInUser: ${loggedInUser}")
    }

    fun clear() {
        this.user = null
        this.accessToken = null
        this.refreshToken = null
    }
}