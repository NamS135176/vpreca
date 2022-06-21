package com.lifecard.vpreca.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class Utils {
    companion object {
        fun openBrowser(context: Context, webUrl: String) {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

}