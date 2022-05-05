package com.lifecard.vpreca

import android.app.Application
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class MyApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
            .installDefaultEventBus()
    }
}