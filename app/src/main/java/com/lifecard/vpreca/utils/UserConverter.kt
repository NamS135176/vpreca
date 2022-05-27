package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.User

object UserConverter {
    @JvmStatic
    fun hideUserPhoneNumber(user: User?): String {
        return user?.telephoneNumber1?.let { hidePhone(it) } ?: ""
    }

    @JvmStatic
    fun hideUserEmail1(user: User?): String {
        return user?.mailAddress1?.let { hideEmail(it) } ?: ""
    }

    @JvmStatic
    fun hideUserEmail2(user: User?): String {
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

    fun getReceiveEmailStatus(flag: String?): String {
        return if (flag == "1") {
            "受け取る"//receive
        } else {
            "受け取らない"//not receive
        }
    }

    fun formatPhone(phone: String): String {
        try {
            return phone.replace(Regex("(\\d{3})(\\d{3})(\\d+)"), "$1-$2-$3")
        } catch (e: Exception) {
        }
        return phone
    }

    @JvmStatic
    fun receiveEmail1(user: User?): String {
        return user?.let { getReceiveEmailStatus(it.mail1RecievFlg) } ?: ""
    }

    @JvmStatic
    fun receiveEmail2(user: User?): String {
        return user?.let { getReceiveEmailStatus(it.mail2RecievFlg) } ?: ""
    }

    @JvmStatic
    fun receiveAdEmail1(user: User?): String {
        return user?.let { getReceiveEmailStatus(it.mail1AdMailRecieveFlg) } ?: ""
    }

    @JvmStatic
    fun receiveAdEmail2(user: User?): String {
        return user?.let { getReceiveEmailStatus(it.mail2AdMailRecieveFlg) } ?: ""
    }
}