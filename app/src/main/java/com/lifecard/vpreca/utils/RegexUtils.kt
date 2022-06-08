package com.lifecard.vpreca.utils

import android.util.Patterns
import java.util.regex.Pattern

class RegexUtils {
    /**
     * sample
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
    (?=\\S+$)          # no whitespace allowed in the entire string
    .{4,}             # anything, at least six places though
    $                 # end-of-string
     */
    companion object {
        /**
         * only half width and underscore
         * check the screen SC08_2
         */
        const val RegexLoginID = "^[a-zA-Z0-9ぁ-んァ-ンｧ-ﾝﾞﾟ_]{6,10}\$"

        /**
         * only roman
         * check the screen SC08_2
         */
        const val RegexNickname = "^[A-Z0-9]{2,18}\$"

        /**
         * 10 or 11 number
         * check the screen SC08_2
         */
        const val RegexPhoneNumber = "^\\d{10}\$|^\\d{11}\$"

        //        const val RegexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}\$"//from internet
        const val RegexEmail = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,64}"//same with ios

        /**
         * only roman included special character  and between 8-12 character
         * check the screen SC09_1
         */
        const val RegexPassword = "^[a-zA-Z0-9@`!#\\\$%()*:+;\\[{,¥|\\-=\\]}.^~/?_]{8,12}\$"

        /**
         * any character with max 20
         */
        const val RegexSecretAnswer = "\\b\\w{1,20}\\b"

        /**
         * at least: 1 digit, 1 lowercase, one uppercase
         */
        private const val RegexOcr = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{12,16}\$"

        /**
         * at least: 1 digit, 1 lowercase, one uppercase
         */
        private const val RegexMobilePhone = "^(070|080|090)+\\d+\$"

        private const val RegexKatakanaFullWidth = "^([ァ-ン]|ー)+\$"
        private const val RegexHiragana = "^[ぁ-ゞ]+$"
        private const val RegexKanji = "^[一-龯]+$"

        private const val RegexHiraganaFullWidth = "^[ぁ-ん]+\$"
        private const val RegexSecondNameFullWidth = "^[一-龥ぁ-ん]+\$"
        private const val RegexGiftNumber = "^[a-zA-Z0-9ぁ-んァ-ンｧ-ﾝﾞﾟ_]{15}\$"
        fun isLoginIdValid(loginId: String?): Boolean {
            return loginId?.let { loginId ->
                Pattern.compile(RegexLoginID).matcher(loginId).matches()
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
                email.length in 0..256 && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            } ?: false
        }

        fun isPasswordValid(password: String?): Boolean {
            return password?.let {
                Pattern.compile(RegexPassword).matcher(password)
                    .matches()
            } ?: false
        }

        fun isSecretAnswerValid(answer: String?): Boolean {
            return answer?.let {
                Pattern.compile(RegexSecretAnswer).matcher(answer)
                    .matches()
            } ?: false
        }
        fun isGiftNumberValid(gift: String?): Boolean {
            return gift?.let {
                Pattern.compile(RegexGiftNumber).matcher(gift)
                    .matches()
            } ?: false
        }

        fun isOcrCode(code: String): Boolean {
            return Pattern.compile(RegexOcr).matcher(code).matches()
        }

        fun isMobilePhone(phone: String): Boolean {
            return Regex(RegexMobilePhone).matches(phone)
        }

        fun isKatakanaFullWidth(text: String?): Boolean {
            return text?.let { Regex(RegexKatakanaFullWidth).matches(it) } ?: false
        }

        fun isHiragana(text: String?): Boolean {
            return text?.let { Regex(RegexHiragana).matches(it) } ?: false
        }

        fun isKanji(text: String?): Boolean {
            return text?.let { Regex(RegexKanji).matches(it) } ?: false
        }

        fun isSecondName(text: String?): Boolean {
            return text?.let { Regex(RegexSecondNameFullWidth).matches(it) } ?: false
        }

        fun isHalfWidth(text: String): Boolean {
            val regex = "[０-９a-zA-Zぁ-んァ-ン一-龥]".toRegex()
            return regex.find(text) == null
        }

        fun isQuestionValid(question: String?): Boolean {
            return !question.isNullOrEmpty()
        }

        fun isCityValid(city: String?): Boolean {
            return !city.isNullOrEmpty()
        }

        fun isGenderValid(gender: String?): Boolean {
            return !gender.isNullOrEmpty()
        }

        fun isAnswerValid(answer: String?): Boolean {
            return !answer.isNullOrEmpty() && answer.length in 0..20 && !isHalfWidth(answer)
        }

        fun formatDisplayPhoneNumber(phone: String?): String {
            return phone?.let {
                val regex = "(\\d{3})(\\d{3})(\\d+)"
                return it.replace(Regex(regex), "\$1-\$2-\$3")
            } ?: phone ?: ""
        }
        fun maskPassword(password: String?): String {
            return password?:""
//            return password?.let {
//                val regex = "(\\d{3})(\\d{3})(\\d+)"
//                return it.replace(Regex(regex), "\$1-\$2-\$3")
//            } ?: password ?: ""
        }

    }
}