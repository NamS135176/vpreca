package com.lifecard.vpreca.ui.webview

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel : ViewModel() {
    val loading = MutableLiveData(false)

    fun handlePageFinished(view: WebView?, url: String?) {
        loading.value = false
    }

    fun handlePageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        loading.value = true
    }
}