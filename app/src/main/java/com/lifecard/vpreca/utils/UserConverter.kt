package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.MemberInfo

object UserConverter {
    @JvmStatic
    fun hideUserPhoneNumber(user: MemberInfo?): String {
        return user?.telephoneNumber1?.let { hidePhone(it) } ?: ""
    }

    @JvmStatic
    fun hideUserEmail1(user: MemberInfo?): String {
        return user?.mailAddress1?.let { hideEmail(it) } ?: ""
    }

    @JvmStatic
    fun hideUserEmail2(user: MemberInfo?): String {
        return user?.mailAddress2?.let { hideEmail(it) } ?: ""
    }

    fun hidePhone(phone: String): String {
        try {
            val result =
                phone.replace(Regex("[\\d](?=[\\d]{4})"), "*")
            println("hidePhone... phone = $phone - result: $result")
            return result
        } catch (e: Exception) {
            println(e.toString())
        }
        return phone
    }

    fun hideEmail(email: String): String {
        try {
            return email.replace(Regex("^(\\w{3})[\\w-]+@([\\w.]+\\w)"), "$1***@$2")
        } catch (e: Exception) {
            println(e.toString())
        }
        return email
    }

    fun hideCreditCard(card: String): String {
        try {
            if (!RegexUtils.isCreditCard(card)) return card
            return card.replace(Regex("(?!(?:\\*\\d){14}\$|(?:\\D*\\d){1,4}\$)\\d"), "*")
        } catch (e: Exception) {
            println(e.toString())
        }
        return card
    }

    fun getReceiveEmailStatus(flag: String?): String {
        return if (flag == "1") {
            "受け取る"//receive
        } else {
            "受け取らない"//not receive
        }
    }

    @JvmStatic
    fun formatPhone(phone: String): String {
        try {
            return phone.replace(Regex("(\\d{3})(\\d{4})(\\d+)"), "$1-$2-$3")
        } catch (e: Exception) {
        }
        return phone
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