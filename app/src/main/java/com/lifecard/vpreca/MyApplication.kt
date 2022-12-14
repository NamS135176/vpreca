package com.lifecard.vpreca

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeInitializer
import org.greenrobot.eventbus.EventBus


@HiltAndroidApp
class MyApplication : MultiDexApplication() {
    override fun onCreate() {
//        applyStrictMode()

        super.onCreate()
        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
            .installDefaultEventBus()

        AppInitializer.getInstance(this).initializeComponent(JodaTimeInitializer::class.java)

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("ja-jp")
        // Call this on the main thread as it may require Activity.restart()
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun applyStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectCustomSlowCalls()
                    .apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            detectResourceMismatches()
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            detectUnbufferedIo()
                        }
                    }

                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }

}