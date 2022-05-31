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
import com.lifecard.vpreca.ui.web_direct.WebDirectFragmentArgs
import com.lifecard.vpreca.utils.WebDirectScreen


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
        val btnToWeb = binding.buttonToWeb
        val btnBySource = binding.btnBalanceSelectSource
        val btnByCode = binding.btnBalanceByCode
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })
        btnBySource.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_amount_select_source) })

        btnToWeb.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_web_direct,
                WebDirectFragmentArgs(screenId = WebDirectScreen.SCREEN_BALANCE_AMOUNT).toBundle()
            )
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })

        return binding.root
    }


}