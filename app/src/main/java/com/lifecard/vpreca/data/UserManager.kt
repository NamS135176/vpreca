package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.model.LoginResponse
import com.lifecard.vpreca.data.model.User
import com.lifecard.vpreca.data.source.SecureStore

class UserManager(private val secureStore: SecureStore) {
    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set
    var accessToken: String? = null
        private set
    var refreshToken: String? = null
        private set
    var loginAction: String? = null
        private set
    var loginId: String? = null
        private set
    var memberNumber: String? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null
    val canCallApi: Boolean
        get() = accessToken != null

    val bearAccessToken: String?
        get() = when (canCallApi) {
            true -> "Bear $accessToken"
            else -> null
        }

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        accessToken = secureStore.getAccessToken()
        refreshToken = secureStore.getRefreshToken()
        loginAction = secureStore.getLoginAction()
        memberNumber = secureStore.getMemberNumber()
        loginId = secureStore.getLoginUserId()
    }

    fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        println("UserManager... setLoggedInUser: ${loggedInUser}")
        secureStore.saveLoginUserId(loggedInUser.loginId)
        secureStore.saveMemberNumber(loggedInUser.memberNumber)
    }

    fun setToken(accessToken: String, refreshToken: String, loginAction: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.loginAction = loginAction

        secureStore.saveAccessToken(accessToken)
        secureStore.saveRefreshToken(refreshToken)
        secureStore.saveLoginAction(loginAction)
    }

    fun setLoggedIn(loginResponse: LoginResponse) {
        setToken(loginResponse.accessToken, loginResponse.refreshToken, loginResponse.action)
        setLoggedInUser(loginResponse.user)
    }

    fun clear() {
        this.user = null
        this.accessToken = null
        this.refreshToken = null
        this.loginAction = null
        this.loginId = null
        this.memberNumber = null
    }

}