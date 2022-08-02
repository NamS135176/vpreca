package com.lifecard.vpreca.data

import android.content.Context
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.data.source.SecureStore

class UserManager(private val secureStore: SecureStore) {
    // in-memory cache of the loggedInUser object
    var memberInfo: MemberInfo? = null
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

    init {
        authToken = secureStore.getAuthToken() ?: AuthToken()
    }

    fun setLoggedMember(appContext: Context, memberResponseContent: MemberResponseContent) {
        memberInfo = memberResponseContent.memberInfo
        if (memberInfo != null
            && (authToken.loginId != memberInfo!!.loginId
                    || authToken.memberNumber != memberInfo!!.memberNumber)
        ) {
            //update auth token with new value
            authToken.loginId = memberInfo!!.loginId
            authToken.memberNumber = memberInfo!!.memberNumber
            secureStore.saveAuthToken(appContext, authToken)
        }
    }

    fun setLoggedIn(appContext: Context, loginResponse: LoginResponse) {
        setLoggedMember(appContext, loginResponse.response)
        authToken.loginId = memberInfo?.loginId
        authToken.memberNumber = memberInfo?.memberNumber
        secureStore.saveAuthToken(appContext, authToken)
    }

    fun saveToken(context: Context, accessToken: String?, refreshToken: String?) {
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return
        }
        authToken.accessToken = accessToken
        authToken.refreshToken = refreshToken
        secureStore.saveAuthToken(context, authToken)
    }

    fun clear() {
        memberInfo = null
        authToken.clear()
    }
}