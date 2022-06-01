package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard

fun CreditCard?.isCardLock(): Boolean {
    return "1" == this?.vcnSecurityLockFlg
}

fun CreditCard.copyCardLockInverse(): CreditCard {
    val newLockStatus = when (this.isCardLock()) {
        true -> "0"//not lock
        else -> "1"//lock
    }
    return this.copy(
        vcnSecurityLockFlg = newLockStatus
    )
}

fun CardInfo?.isCardInfoLock(): Boolean {
    return "1" == this?.vcnSecurityLockFlg
}

fun CardInfo.copyCardInfoLockInverse(): CardInfo {
    val newLockStatus = when (this.isCardInfoLock()) {
        true -> "0"//not lock
        else -> "1"//lock
    }
    return this.copy(
        vcnSecurityLockFlg = newLockStatus
    )
}