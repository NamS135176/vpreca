package com.lifecard.vpreca

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeInitializer
import org.greenrobot.eventbus.EventBus
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


@HiltAndroidApp
class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
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


}