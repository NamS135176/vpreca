package com.lifecard.vpreca.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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