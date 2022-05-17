package com.lifecard.vpreca.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {
    companion object {
        fun isAcceptTermOfUseFirstTime(appContext: Context): Boolean {
            val sharedPreferences =
                appContext.getSharedPreferences("VPrecaPref", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("accept_term_of_use_first_time", false)
        }

        fun setAcceptTermOfUseFirstTime(appContext: Context, value: Boolean) {
            val sharedPreferences =
                appContext.getSharedPreferences("VPrecaPref", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("accept_term_of_use_first_time", value)
                commit()
            }
        }

        fun isFingerprintSetting(appContext: Context): Boolean {
            val sharedPreferences =
                appContext.getSharedPreferences("VPrecaPref", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("fingerprint_setting", false)
        }

        fun setFingerprintSetting(appContext: Context, value: Boolean) {
            val sharedPreferences =
                appContext.getSharedPreferences("VPrecaPref", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("fingerprint_setting", value)
                commit()
            }
        }
    }
}