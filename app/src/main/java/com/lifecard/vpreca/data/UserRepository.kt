package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.api.ApiService
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

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        val accessToken = secureStore.getAccessToken()
        val refreshToken = secureStore.getRefreshToken()
        println("UserRepository... init: accessToken=${accessToken}")
        if (accessToken != null) {
            val fakeUser = User(
                UUID.randomUUID().toString(),
                "The Anh",
                "anhndt@vn-sis.com",
                accessToken = accessToken,
                refreshToken = refreshToken!!
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
                    accessToken = accessToken!!,
                    refreshToken = refreshToken!!
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
    }
}