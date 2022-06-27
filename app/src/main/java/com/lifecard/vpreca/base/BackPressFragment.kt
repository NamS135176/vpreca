package com.lifecard.vpreca.base

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.utils.fragmentFindNavController

open class BackPressFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBeforeBackPress()
                fragmentFindNavController().popBackStack()
            }
        })
    }

    open fun onBeforeBackPress() {

    }
}