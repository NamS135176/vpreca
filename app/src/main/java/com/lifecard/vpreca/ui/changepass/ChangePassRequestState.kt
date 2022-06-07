package com.lifecard.vpreca.ui.changepass

import com.lifecard.vpreca.data.model.ChangeInfoMemberData
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.exception.ErrorMessageException

data class ChangePassRequestState(
    val success: MemberInfo? = null,
    val errorText: String? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
