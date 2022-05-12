package com.lifecard.vpreca.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.lifecard.vpreca.R


fun Fragment.fragmentFindNavController(): NavController {
    return requireActivity().findNavController(R.id.nav_host_fragment_content_main)
}

fun Fragment.hideToolbar(activity: Activity) {

    (activity as? AppCompatActivity)?.supportActionBar?.hide()
}

fun Fragment.showToolbar(activity: Activity) {
    (activity as? AppCompatActivity)?.supportActionBar?.show()
}

fun View.viewFindNavController(ctx: Context): NavController {
    return (ctx as Activity).findNavController(R.id.nav_host_fragment_content_main)
}

fun Fragment.setLightStatusBar() = try {
    val window = requireActivity().window

    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,//show light
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        var flags = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } // add LIGHT_STATUS_BAR to flag
        window.decorView.systemUiVisibility = flags
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = getColor(requireContext(), R.color.white)
    } else {
    }

} catch (e: Exception) {
}

fun Fragment.clearLightStatusBar() = try {
    val window = requireActivity().window
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            0,//Remove
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        var flags = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } // use XOR here for remove LIGHT_STATUS_BAR from flags
        window.decorView.systemUiVisibility = flags
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = getColor(requireContext(), R.color.primary)
    } else {
    }

} catch (e: Exception) {
}