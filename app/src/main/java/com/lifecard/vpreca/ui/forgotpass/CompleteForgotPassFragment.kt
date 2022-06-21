package com.lifecard.vpreca.ui.forgotpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentForgotPassCompleteBinding


class CompleteForgotPassFragment : Fragment() {

    private var _binding : FragmentForgotPassCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPassCompleteBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { backFunction() }
        })

        val btnComplete = binding.btnCompleteForgot
        btnComplete.setOnClickListener { backFunction() }

        return binding.root
    }

    fun backFunction(){
        findNavController().navigate(R.id.action_forgot_complete_to_login)
    }

}