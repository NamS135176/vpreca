package com.lifecard.vpreca.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

open class NoToolbarFragment : Fragment() {
    private lateinit var lifecycleObserver: DefaultLifecycleObserver

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                hideToolbar()
                owner.lifecycle.removeObserver(lifecycleObserver)
            }
        }
        requireActivity().lifecycle.addObserver(lifecycleObserver)
    }
}
