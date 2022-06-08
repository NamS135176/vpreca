package com.lifecard.vpreca.data.source

import com.lifecard.vpreca.data.model.AuthToken

interface AuthTokenStore {
    fun saveAuthToken(authToken: AuthToken)
    fun getAuthToken(): AuthToken?
}