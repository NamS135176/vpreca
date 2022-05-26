package com.lifecard.vpreca.utils

import java.util.regex.Pattern

class RegexUtils {
    companion object {
        /**
         * only half width and underscore
         * check the screen SC08_2
         */
        const val RegexLoginID = "[a-zA-Z0-9ぁ-んァ-ンｧ-ﾝﾞﾟ_]+\$"

        /**
         * only roman
         * check the screen SC08_2
         */
        const val RegexNickname = "^[a-zA-Z0-9]{2,18}\$"

        /**
         * 10 or 11 number
         * check the screen SC08_2
         */
        const val RegexPhoneNumber = "/^\\d{10}\$/"

        const val RegexEmail = "^\\S+@\\S+\\.\\S+\$"

        fun isLoginIdValid(loginId: String?): Boolean {
            return loginId?.let { id ->
                id.length in 6..10 && Pattern.compile(RegexLoginID).matcher(id).matches()
            } ?: false
        }

        fun isNicknameValid(nickname: String?): Boolean {
            return nickname?.let {
                Pattern.compile(RegexNickname).matcher(nickname)
                    .matches()
            } ?: false
        }

        fun isPhoneNumberValid(phoneNumber: String?): Boolean {
            return phoneNumber?.let {
                Pattern.compile(RegexPhoneNumber).matcher(phoneNumber)
                    .matches()
            } ?: false
        }

        fun isEmailValid(email: String?): Boolean {
            return email?.let {
                Pattern.compile(RegexEmail).matcher(email)
                    .matches()
            } ?: false
        }
    }
}