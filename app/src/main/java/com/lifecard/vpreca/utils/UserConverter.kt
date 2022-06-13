package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.MemberInfo

object UserConverter {
    @JvmStatic
    fun hideUserEmail1(user: MemberInfo?): String {
        return user?.mailAddress1?.let { RegexUtils.hideEmail(it) } ?: ""
    }

    @JvmStatic
    fun hideUserEmail2(user: MemberInfo?): String {
        return user?.mailAddress2?.let { RegexUtils.hideEmail(it) } ?: ""
    }

    private fun getReceiveEmailStatus(flag: String?): String {
        return if (flag == "1") {
            "受け取る"//receive
        } else {
            "受け取らない"//not receive
        }
    }

    @JvmStatic
    fun formatPhone(phone: String): String {
        return RegexUtils.formatDisplayPhoneNumber(phone)
    }

    @JvmStatic
    fun formatHideDisplayPhoneNumber(user: MemberInfo?): String {
        return user?.let { RegexUtils.formatHideDisplayPhoneNumber(it.telephoneNumber1) } ?: ""
    }

    @JvmStatic
    fun receiveEmail1(user: MemberInfo?): String {
        return user?.let { getReceiveEmailStatus(it.mail1RecievFlg) } ?: ""
    }

    @JvmStatic
    fun receiveEmail2(user: MemberInfo?): String {
        return user?.let { getReceiveEmailStatus(it.mail2RecievFlg) } ?: ""
    }

    @JvmStatic
    fun receiveAdEmail1(user: MemberInfo?): String {
        return user?.let { getReceiveEmailStatus(it.mail1AdMailRecieveFlg) } ?: ""
    }

    @JvmStatic
    fun receiveAdEmail2(user: MemberInfo?): String {
        return user?.let { getReceiveEmailStatus(it.mail2AdMailRecieveFlg) } ?: ""
    }

    @JvmStatic
    fun convertReceiveState(state: String): String {
        if (state == "0") {
            return "受け取らない"
        } else return "受け取る"
    }
}