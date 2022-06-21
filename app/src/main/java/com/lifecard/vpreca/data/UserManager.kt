package com.lifecard.vpreca.data

import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.data.source.SecureStore

class UserManager(private val secureStore: SecureStore) {
    // in-memory cache of the loggedInUser object
    var memberInfo: MemberInfo? = null
        private set
    var memberSubInfo: MemberSubInfo? = null
        private set
    var accessToken: String? = null
        private set
        get() {
            return authToken.accessToken
        }
    var refreshToken: String? = null
        private set
        get() {
            return authToken.refreshToken
        }
    var loginId: String? = null
        private set
        get() {
            return authToken.loginId ?: memberInfo?.loginId
        }
    var memberNumber: String? = null
        private set
        get() {
            return authToken.memberNumber ?: memberInfo?.memberNumber
        }

    var authToken: AuthToken = AuthToken()

    val isLoggedIn: Boolean
        get() = !authToken.isEmpty()
    val canCallApi: Boolean
        get() = accessToken != null

    val bearAccessToken: String?
        get() = when (canCallApi) {
            true -> "Bear $accessToken"
            else -> null
        }

    init {
        authToken = secureStore.getAuthToken() ?: AuthToken()
    }

    fun setLoggedMember(memberResponseContent: MemberResponseContent) {
        memberInfo = memberResponseContent.memberInfo
        memberSubInfo = memberResponseContent.memberSubInfo
        if (memberInfo != null
            && (authToken.loginId != memberInfo!!.loginId
                    || authToken.memberNumber != memberInfo!!.memberNumber)
        ) {
            //update auth token with new value
            authToken.loginId = memberInfo!!.loginId
            authToken.memberNumber = memberInfo!!.memberNumber
            secureStore.saveAuthToken(authToken)
        }
    }

    fun setLoggedIn(loginResponse: LoginResponse) {
        setLoggedMember(loginResponse.response)
        authToken.loginId = memberInfo?.loginId
        authToken.memberNumber = memberInfo?.memberNumber
        secureStore.saveAuthToken(authToken)
    }

    fun saveToken(accessToken: String?, refreshToken: String?) {
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return
        }
        authToken.accessToken = accessToken
        authToken.refreshToken = refreshToken
        secureStore.saveAuthToken(authToken)
    }

    fun clear() {
        this.memberInfo = null
        this.memberSubInfo = null
        this.authToken.clear()
    }

}