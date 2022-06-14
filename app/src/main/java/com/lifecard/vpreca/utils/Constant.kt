package com.lifecard.vpreca.utils

class Constant {
    companion object {
        const val SECURE_AUTH_TOKEN = "vpreca_auth_token"
        const val SECURE_USER_ID = "vpreca_login_user_id"
        const val SECURE_MEMBER_NUMBER = "vpreca_login_member_id"
        const val SECURE_ACCESS_TOKEN = "vpreca_access_token"
        const val SECURE_REFRESH_TOKEN = "vpreca_refresh_token"
        const val BIOMETRIC_ALGORITHM = "ecdsa-with-SHA256"

        const val WEB_DIRECT_BASE_URL = "https://vpcevssl.lifecard.co.jp/LW01/%s.do"

        const val FEE_TYPE_BALANCE = "31"
        const val FEE_TYPE_ISSUE = "34"

        //dev
//        const val API_BASE_URL = "https://by4h7iozy5.execute-api.ap-southeast-1.amazonaws.com/dev/"
        //staging
        const val API_BASE_URL =
            "https://xvojcilpig.execute-api.ap-southeast-1.amazonaws.com/staging/"

        const val GOOGLE_VISION_API = "https://vision.googleapis.com/"
    }
}

class WebDirectScreen {
    companion object {
        const val SCREEN_CREDIT_CARD_INFO = "LW1401SC01"
        const val SCREEN_ISSUE_CARD_WITH_CREDIT_CARD = "LW0108SC01"
        const val SCREEN_BALANCE_AMOUNT = "LW2702SC01"
    }
}