package com.lifecard.vpreca.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.AlertDialogFragment

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.showPopupMessage(title: String? = null, message: String): Fragment? = try {
    println("showPopupMessage... title = $title, message = $message")
    val supportFragmentManager = requireActivity().supportFragmentManager
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

fun Fragment.hidePopupMessage() = try {
    val supportFragmentManager = requireActivity().supportFragmentManager
    val fragment =
        supportFragmentManager.findFragmentByTag(AlertDialogFragment.FRAGMENT_TAG)
    fragment?.let {
        supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
} catch (e: Exception) {
    print(e)
}

fun Fragment.showInternetTrouble(): Fragment? {
    return showPopupMessage(
        title = getString(R.string.error_no_internet_connection_title),
        message = getString(R.string.error_no_internet_connection_content)
    )
}
