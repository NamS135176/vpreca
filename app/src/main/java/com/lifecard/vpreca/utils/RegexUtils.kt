package com.lifecard.vpreca.utils

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
         * only half width and underscore from range 6-10 chars
         * check the screen SC08_2
         */
        private const val RegexLoginID = "^[a-zA-Z0-9_-]{6,10}\$"

        /**
         * Regex detect text has special character excludes underscore and -
         * We will using this regex for check login id contain any special characters
         * check the screen SC08_2
         */
        private const val RegexSpecialCharacterForLoginID = "[^\\w\\s-_]"

        /**
         * only roman
         * check the screen SC08_2
         */
        private const val RegexNickname = "^[A-Z]{2,19}\$"

        /**
         * 10 or 11 number
         * check the screen SC08_2
         */
        private const val RegexPhoneNumber = "^\\d{11}\$"

        private const val RegexEmail =
            "^[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+\$"

        /**
         * only roman included special character  and between 8-12 character
         * check the screen SC09_1
         */
        private const val RegexPassword =
            "^[a-zA-Z0-9&@`'\"!#\\\$%()*:+;\\[{,¥|\\-=\\]}.^~/?_]{8,12}\$"

        /**
         * any character with max 20
         */
        private const val RegexSecretAnswer = "\\b\\w{1,20}\\b"

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

        /**
         * Name katakana full width with space
         */
        private const val RegexKanaNameFullWidth = "^([ァ-ン\\s　]{1,19})\$"

        /**
         * Check name full width with space
         */
        private const val RegexNameFullWidth = "^[Ａ-ｚ０-９ぁ-んァ-ン一-龥・|ー\\s　]{1,19}\$"
        private const val RegexGiftNumber = "^[a-zA-Z0-9]{15}\$"

        /**
         * Fullwidth for hiragana, katakan, kanji, number and alphabet
         */
        private const val RegexFullWidth = "^[ａ-ｚＡ-Ｚ０-９ぁ-んァ-ン一-龥・|ー]+\$"

        /**
         * Fullwidth for hiragana, katakan, kanji, number and alphabet
         */
        private const val RegexAnswer = "^[ａ-ｚＡ-Ｚ０-９ぁ-んァ-ン一-龥・|ー]{1,20}\$"

        /**
         * see sample https://support.sumologic.com/hc/en-us/articles/205565718-Regular-expression-for-masking-credit-card-numbers
         */
        private const val RegexCreditCard =
            "((?:(?:4\\d{3})|(?:5[1-5]\\d{2})|6(?:011|5[0-9]{2}))(?:-?|\\040?)(?:\\d{4}(?:-?|\\040?)){3}|(?:3[4,7]\\d{2})(?:-?|\\040?)\\d{6}(?:-?|\\040?)\\d{5})"

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
            return phoneNumber?.let { isMobilePhone(it) } ?: false
        }


        fun isEmailValid(email: String?): Boolean {
            return email?.let {
                email.length in 0..256 && Regex(RegexEmail).matches(email)
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

        private fun isMobilePhone(phone: String): Boolean {
            return Regex(RegexMobilePhone).matches(phone) && Regex(RegexPhoneNumber).matches(phone)
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

        fun isNameFullWidth(text: String?): Boolean {
            return text?.let { Regex(RegexNameFullWidth).matches(it) } ?: false
        }

        fun isKanaNameFullWidth(text: String?): Boolean {
            return text?.let { Regex(RegexKanaNameFullWidth).matches(it) } ?: false
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
            return answer?.let { Regex(RegexAnswer).matches(it) } ?: false
        }

        fun formatDisplayPhoneNumber(phone: String?): String {
            return phone?.let {
                val regex =
                    if (phone.length == 10) "(\\d{3})(\\d{3})(\\d+)" else "(\\d{3})(\\d{4})(\\d+)"
                return it.replace(Regex(regex), "\$1-\$2-\$3")
            } ?: phone ?: ""
        }

        /**
         * @return ex: ***-***-6090
         */
        fun formatHideDisplayPhoneNumber(phone: String?): String {
            return phone?.let {
                if (phone.length == 10) {
                    val regex = "(\\d{3})(\\d{3})(\\d+)"
                    return it.replace(Regex(regex), "***-***-\$3")
                } else {
                    val regex = "(\\d{3})(\\d{4})(\\d+)"
                    return it.replace(Regex(regex), "***-****-\$3")
                }
            } ?: phone ?: ""
        }

        fun maskPassword(password: String?): String {
            return password ?: ""
        }

        fun hidePassword(password: String?): String {
            try {
                var result = ""
                for (i in 0..(password?.length?.minus(1))!!) {
                    result += "*"
                }
                return result
            } catch (e: Exception) {
                println(e.toString())
            }
            return password!!
        }

        fun checkLoginIdContainSpecialCharacter(loginId: String?): Boolean {
            return loginId?.let { Regex(RegexSpecialCharacterForLoginID).matches(it) } ?: false
        }

        fun isFullWidth(text: String?): Boolean {
            return text?.let { Regex(RegexFullWidth).matches(it) } ?: false
        }

        fun isCreditCard(card: String?): Boolean {
            return card?.let { Regex(RegexCreditCard).matches(it) } ?: false
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
                if (!isCreditCard(card)) return card
                return card.replace(Regex("(?!(?:\\*\\d){14}\$|(?:\\D*\\d){1,4}\$)\\d"), "*")
            } catch (e: Exception) {
                println(e.toString())
            }
            return card
        }

    }
}