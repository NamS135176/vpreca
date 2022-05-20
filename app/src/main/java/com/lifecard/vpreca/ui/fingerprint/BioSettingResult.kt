package com.lifecard.vpreca.ui.fingerprint

import com.lifecard.vpreca.data.model.BioChallenge

data class BioSettingResult(
    val success: BioChallenge? = null,
    val error: Int? = null,
    val bioStatus: Int? = null
)
