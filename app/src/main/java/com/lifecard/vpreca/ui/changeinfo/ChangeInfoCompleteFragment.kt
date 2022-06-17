package com.lifecard.vpreca.ui.changeinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangeInfoCompleteBinding

class ChangeInfoCompleteFragment : Fragment() {

    private var _binding: FragmentChangeInfoCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeInfoCompleteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.nav_home, inclusive = false)
                }
            })

        val btnConfirm = binding.btnCompleteForgot

        btnConfirm.setOnClickListener { findNavController().popBackStack(R.id.nav_home, inclusive = false) }

        return binding.root
    }

}