package com.lifecard.vpreca.utils

import android.content.Context

class PreferenceHelper {
    companion object {
        const val PREF_NAME = "VPrecaPref"
        fun isAcceptTermOfUseFirstTime(appContext: Context): Boolean {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("accept_term_of_use_first_time", false)
        }

        fun setAcceptTermOfUseFirstTime(appContext: Context, value: Boolean) {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("accept_term_of_use_first_time", value)
                commit()
            }
        }

        fun isEnableBiometricSetting(appContext: Context): Boolean {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("fingerprint_setting", false)
        }

        fun setEnableBiometricSetting(appContext: Context, value: Boolean) {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("fingerprint_setting", value)
                commit()
            }
        }

        fun clearDueLogout(appContext: Context) {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val isAcceptTermOfUseFirstTime = isAcceptTermOfUseFirstTime(appContext)
            with(sharedPreferences.edit()) {
                clear()
                if (isAcceptTermOfUseFirstTime) {
                    putBoolean("accept_term_of_use_first_time", isAcceptTermOfUseFirstTime)
                }
                commit()
            }
        }
    }
}