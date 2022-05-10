package com.lifecard.vpreca.exception

class ErrorMessageException(val messageResId: Int, val exception: Exception? = null) :
    Exception() {
}