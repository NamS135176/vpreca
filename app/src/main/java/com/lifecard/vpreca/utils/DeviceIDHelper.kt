package com.lifecard.vpreca.utils

import android.content.Context
import java.util.*

class DeviceIDHelper {
    companion object {
        private const val PREF_NAME = "VPrecaDeviceIDPref"
        fun getDeviceId(appContext: Context): String {
            val sharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            var deviceId = sharedPreferences.getString("device_id", null)
            if (deviceId.isNullOrEmpty()) {
                deviceId = UUID.randomUUID().toString()
                deviceId.replace("-", "")
                with(sharedPreferences.edit()) {
                    putString("device_id", deviceId)
                    apply()
                }
            }

            return deviceId
        }
    }
}