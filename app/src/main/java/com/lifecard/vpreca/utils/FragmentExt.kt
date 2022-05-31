package com.lifecard.vpreca.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}