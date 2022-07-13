package com.lifecard.vpreca.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URI
import java.util.*


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

        fun randomProcessId(): String {
            return randomString(6)
        }

        private fun randomString(stringLength: Int): String {
            val list = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
            var randomS = ""
            for (i in 1..stringLength) {
                randomS += list[getRandomNumber(0, list.size - 1)]
            }
            return randomS
        }

        private fun getRandomNumber(min: Int, max: Int): Int {
            return Random().nextInt((max - min) + 1) + min
        }

        /**
         * parse path of url
         */
        fun getMessageTypeFromUrl(uri: URI): String? {
            try {
//                val uri = URI(url)
                val path = uri.path
                val pos = path.lastIndexOf('/') + 1
                val messageType = path.substring(pos)
                return messageType
            } catch (e: Exception) {
                println(e)
            }
            return null
        }
    }


}