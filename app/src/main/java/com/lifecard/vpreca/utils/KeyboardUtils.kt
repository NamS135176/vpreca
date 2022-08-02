package com.lifecard.vpreca.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class KeyboardUtils {
    companion object {
        fun hideKeyboard(appContext: Context, view: View) {
            val imm: InputMethodManager? =
                appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

}