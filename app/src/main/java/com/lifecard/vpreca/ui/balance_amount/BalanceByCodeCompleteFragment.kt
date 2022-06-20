package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeCompleteBinding


class BalanceByCodeCompleteFragment : Fragment() {

    private var _binding: FragmentBalanceByCodeCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBalanceByCodeCompleteBinding.inflate(inflater, container, false)

        val btnSubmit = binding.btnSubmitPolicy

        btnSubmit.setOnClickListener { findNavController().popBackStack(R.id.nav_home, inclusive = false)}
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_home, inclusive = false)
            }
        })

        return binding.root
    }


}