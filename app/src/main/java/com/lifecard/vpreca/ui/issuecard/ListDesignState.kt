package com.lifecard.vpreca.ui.issuecard

import com.lifecard.vpreca.data.model.ListDesignResponseContent
import com.lifecard.vpreca.exception.ErrorMessageException

data class ListDesignState(
    val success: ListDesignResponseContent? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
