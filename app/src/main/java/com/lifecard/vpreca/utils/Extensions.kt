package com.lifecard.vpreca.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.custom.DrawerMenuLayout


fun Fragment.fragmentFindNavController(): NavController {
    var navController: NavController = findNavController()
//    try {
//        if (navController == null) {
//            navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main)
//        }
//    } catch (e: Exception) {
//    }

    return navController
}

fun Fragment.hideToolbar() {
    try {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    } catch (e: Exception) {
    }
}

fun Fragment.showToolbar() {
    try {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
    } catch (e: Exception) {
    }
}

fun View.viewFindNavController(): NavController {
    return (context as Activity).findNavController(R.id.nav_host_fragment_content_main)
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
        }
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

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Fragment.navigateToLogin() = try {
    findNavController().setGraph(R.navigation.authentication_navigation)
} catch (e: Exception) {

}

fun DrawerMenuLayout.navigateToLogin() = try {
    viewFindNavController().setGraph(R.navigation.authentication_navigation)
} catch (e: Exception) {

}

fun Fragment.navigateToHome() = try {
    val navController = findNavController()
    navController.setGraph(R.navigation.main_navigation)
    val activity = requireActivity() as AppCompatActivity
    val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
    val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
    activity.setupActionBarWithNavController(navController, appBarConfiguration)
} catch (e: Exception) {

}

fun Fragment.lockDrawer() = try {
    val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
} catch (e: Exception) {

}

fun AppCompatActivity.lockDrawer() = try {
    val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
} catch (e: Exception) {

}

fun Fragment.unlockDrawer() = try {
    val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
} catch (e: Exception) {

}