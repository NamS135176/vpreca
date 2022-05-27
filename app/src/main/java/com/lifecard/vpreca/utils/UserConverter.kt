package com.lifecard.vpreca.utils

import com.lifecard.vpreca.data.model.User

object UserConverter {
    @JvmStatic
    fun hideUserPhoneNumber(user: User?): String {
        return user?.telephoneNumber1 ?: ""
    }

    @JvmStatic
    fun hideUserEmail1(user: User?): String {
        return user?.mailAddress1 ?: ""
    }

    @JvmStatic
    fun hideUserEmail2(user: User?): String {
        return user?.mailAddress2 ?: ""
    }

    private fun hideEmail(email: String): String {
        try {
            val splits = email.split("@")

        } catch (e: Exception) {
            println(e.toString())
        }
        return email
    }

    private fun getReceiveEmailStatus(flag: String?): String {
        return if (flag == "1") {
            "受け取る"//receive
        } else {
            "受け取らない"//not receive
        }
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