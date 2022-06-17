package com.lifecard.vpreca.ui.smsverify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentOvertimesVerifyBinding

class OvertimesVerifyFragment : Fragment() {
    private var _binding: FragmentOvertimesVerifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOvertimesVerifyBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { findNavController().navigate(R.id.nav_login) }
        })
        val btnComplete = binding.btnOvertimes
        btnComplete.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }
        return binding.root
    }

}