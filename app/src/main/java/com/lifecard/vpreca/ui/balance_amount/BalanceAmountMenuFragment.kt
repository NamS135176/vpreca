package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardMainBinding


class BalanceAmountMenuFragment : Fragment() {
    private var _binding: FragmentBalanceAmountMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBalanceAmountMenuBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarGiftThird.btnBack
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })

        return binding.root
    }


}