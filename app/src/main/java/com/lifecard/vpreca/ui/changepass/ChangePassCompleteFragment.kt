package com.lifecard.vpreca.ui.changepass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePassCompleteBinding

class ChangePassCompleteFragment : Fragment() {
    private var _binding: FragmentChangePassCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangePassCompleteBinding.inflate(inflater, container, false)
        val btnSubmit = binding.btnSubmitPolicy
        btnSubmit.setOnClickListener { findNavController().popBackStack(R.id.nav_home, inclusive = false) }
        return binding.root
    }

}