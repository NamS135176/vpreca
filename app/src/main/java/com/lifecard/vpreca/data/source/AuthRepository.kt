package com.lifecard.vpreca.data.source

import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.api.RetrofitBuilder
import com.lifecard.vpreca.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class AuthRepository {
    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    suspend fun login(username: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                var response = RetrofitBuilder.apiService.login(username, password)
                val fakeUser = User(
                    UUID.randomUUID().toString(),
                    "The Anh",
                    "anhndt@vn-sis.com",
                    response.accessToken
                )
                setLoggedInUser(fakeUser)
                Result.Success(fakeUser)
            } catch (e: Throwable) {
                println("LoginDataSource... login has error ${e}")
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}