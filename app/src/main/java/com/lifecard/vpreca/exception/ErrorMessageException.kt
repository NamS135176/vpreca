package com.lifecard.vpreca.exception

class ErrorMessageException(
    val messageResId: Int? = null,
    val errorMessage: String? = null,
    val exception: Exception? = null
) :
    Exception() {
}