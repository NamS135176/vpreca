package com.lifecard.vpreca.ui.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.termofuse.TermOfUseFragment

class WebViewActivity : AppCompatActivity() {
    companion object {
        var EXTRA_WEB_URL = "web_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webUrl = intent.extras?.getString(EXTRA_WEB_URL)
        if (savedInstanceState == null && webUrl != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WebViewFragment.newInstance(webUrl))
                .commitNow()
        }
    }
}