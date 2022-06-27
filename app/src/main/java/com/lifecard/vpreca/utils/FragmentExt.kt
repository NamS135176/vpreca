package com.lifecard.vpreca.utils

import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.AlertDialogFragment

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.showPopupMessage(
    title: String? = null,
    message: String,
    buttonCancel: Int? = R.string.button_close
): Fragment? = try {
    val supportFragmentManager = childFragmentManager
    var fragment =
        supportFragmentManager.findFragmentByTag(AlertDialogFragment.FRAGMENT_TAG)
    if (fragment == null) {
        fragment = AlertDialogFragment.newInstance(
            title = title,
            message = message,
            buttonCancel = buttonCancel
        )
        supportFragmentManager.beginTransaction()
            .add(fragment, AlertDialogFragment.FRAGMENT_TAG)
            .commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
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
        message = getString(R.string.error_no_internet_connection_content),
        buttonCancel = R.string.button_close
    )
}

fun Fragment.openBrowser(webUrl: String) {
    Utils.openBrowser(requireContext(), webUrl)
}

fun Fragment.closeKeyBoard() {
    try {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    } catch (e: Exception) {

    }
}

fun Fragment.mainGraphActionNavigateHome() = try {
    val navController = findNavController()
    val count = navController.backQueue.count()
    val previous = if (count > 1) navController.backQueue[count - 2] else null

    if (previous?.destination?.id == R.id.nav_home) {
        navController.popBackStack()
    } else {
        navController.navigate(R.id.nav_home, null, navOptions {
            popUpTo(R.id.nav_home) {
                inclusive = true
                saveState = true
            }
        })
    }
} catch (e: Exception) {

}

fun Fragment.closeApp() {
    if (Build.VERSION.SDK_INT < 21) {
        requireActivity().finishAffinity()
    } else if (Build.VERSION.SDK_INT >= 21) {
        requireActivity().finishAndRemoveTask()
    }
    try {
        System.exit(0)
    } catch (e: Exception) {}
}