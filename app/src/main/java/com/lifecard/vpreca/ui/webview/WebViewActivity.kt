package com.lifecard.vpreca.ui.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.lifecard.vpreca.R

class WebViewActivity : AppCompatActivity() {
    companion object {
        var EXTRA_WEB_URL = "web_url"
        fun createBundle(webUrl: String): Bundle {
            return bundleOf("web_url" to webUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webUrl = intent.extras?.getString(EXTRA_WEB_URL)
        if (savedInstanceState == null) {
            if (webUrl != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WebViewFragment.newInstance(webUrl))
                    .commitNow()
            } else {
                finish()
            }
        }
    }
}