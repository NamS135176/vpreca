package com.lifecard.vpreca

import android.app.Application
import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeInitializer
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class MyApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
            .installDefaultEventBus()

        AppInitializer.getInstance(this).initializeComponent(JodaTimeInitializer::class.java)
    }
}