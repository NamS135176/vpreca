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
    var refreshToken: String? = null
        private set
    var loginId: String? = null
        private set
    var memberNumber: String? = null
        private set

    val isLoggedIn: Boolean
        get() = memberInfo != null
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
        memberNumber = secureStore.getMemberNumber()
        loginId = secureStore.getLoginUserId()
    }

    fun setToken(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken

        secureStore.saveAccessToken(accessToken)
        secureStore.saveRefreshToken(refreshToken)
    }

    fun setLoggedMember(memberResponseContent: MemberResponseContent) {
        memberInfo = memberResponseContent.memberInfo
        memberSubInfo = memberResponseContent.memberSubInfo
        this.memberNumber = memberInfo!!.memberNumber
        this.loginId = memberInfo!!.loginId
        secureStore.saveLoginUserId(memberInfo!!.loginId)
        secureStore.saveMemberNumber(memberInfo!!.memberNumber)
    }

    fun setLoggedIn(loginResponse: LoginResponse) {
        setToken(loginResponse.accessToken, loginResponse.refreshToken)
        setLoggedMember(loginResponse.brandPrecaApi.response)
    }

    fun clear() {
        this.memberInfo = null
        this.memberSubInfo = null
        this.accessToken = null
        this.refreshToken = null
        this.loginId = null
        this.memberNumber = null
    }

}