package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SignupInputState
import java.text.SimpleDateFormat
import java.util.*

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

fun SignupInputState.getKanaName(): String {
    return "${kanaFirstName ?: ""} ${kanaLastName ?: ""}"
}

fun SignupInputState.getFurigana(): String {
    return "${hiraFirstName ?: ""} ${hiraLastName ?: ""}"
}

fun SignupInputState.getDate(): Date? {
    return try {
        date?.let { SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).parse(date) }
    } catch (e: java.lang.Exception) {
        null
    }
}

fun SignupInputState.formatPhoneNumber(): String? {
    return RegexUtils.formatDisplayPhoneNumber((phone))
}

fun SignupInputState.maskPassword(): String? {
    return RegexUtils.maskPassword((password))
}

private fun getReceiveEmailStatus(flag: String?): String {
    return if (flag == "1") {
        "受け取る"//receive
    } else {
        "受け取らない"//not receive
    }
}

fun SignupInputState.receiveEmail1(): String {
    return mail1RecievFlg?.let { getReceiveEmailStatus(mail1RecievFlg) } ?: ""
}

fun SignupInputState.receiveEmail2(): String {
    return mail2RecievFlg?.let { getReceiveEmailStatus(mail2RecievFlg) } ?: ""
}

fun SignupInputState.receiveAdEmail1(): String {
    return mail1AdMailRecieveFlg?.let { getReceiveEmailStatus(mail1AdMailRecieveFlg) } ?: ""
}

fun SignupInputState.receiveAdEmail2(): String {
    return mail2AdMailRecieveFlg?.let { getReceiveEmailStatus(mail2AdMailRecieveFlg) } ?: ""
}