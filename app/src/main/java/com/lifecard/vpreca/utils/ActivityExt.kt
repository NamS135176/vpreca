package com.lifecard.vpreca.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.AlertDialogFragment
import com.lifecard.vpreca.base.LoadingDialogFragment

fun AppCompatActivity.showPopupMessage(title: String? = null, message: String): Fragment? = try {
    val supportFragmentManager = supportFragmentManager
    var fragment =
        supportFragmentManager.findFragmentByTag(AlertDialogFragment.FRAGMENT_TAG)
    if (fragment == null) {
        fragment = AlertDialogFragment.newInstance(title = title, message = message)
        supportFragmentManager.beginTransaction()
            .add(fragment, AlertDialogFragment.FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    fragment
} catch (e: Exception) {
    print(e)
    null
}

fun AppCompatActivity.hideAlert() = try {
    val supportFragmentManager = supportFragmentManager
    val fragment =
        supportFragmentManager.findFragmentByTag(AlertDialogFragment.FRAGMENT_TAG)
    fragment?.let {
        supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
} catch (e: Exception) {
    print(e)
}

fun AppCompatActivity.showInternetTrouble(): Fragment? {
    return showPopupMessage(
        title = getString(R.string.error_no_internet_connection_title),
        message = getString(R.string.error_no_internet_connection_content)
    )
}

fun AppCompatActivity.hideLoadingDialog() = try {
    val supportFragmentManager = supportFragmentManager
    val fragment =
        supportFragmentManager.findFragmentByTag(LoadingDialogFragment.FRAGMENT_TAG)
    fragment?.let {
        supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
} catch (e: Exception) {
    print(e)
}

fun AppCompatActivity.showLoadingDialog(): Fragment? = try {
    val supportFragmentManager = supportFragmentManager
    var fragment =
        supportFragmentManager.findFragmentByTag(LoadingDialogFragment.FRAGMENT_TAG)
    if (fragment == null) {
        fragment = LoadingDialogFragment()
        supportFragmentManager.beginTransaction()
            .add(fragment, LoadingDialogFragment.FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    fragment
} catch (e: Exception) {
    print(e)
    null
}