package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceSelectSourceConfirmBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeSelectSoureConfirmFragmentArgs

class BalanceSelectSourceConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceSelectSourceConfirmFragment()
    }

    private var _binding: FragmentBalanceSelectSourceConfirmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BalanceSelectSourceConfirmViewModel
    private val args: BalanceSelectSourceConfirmFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBalanceSelectSourceConfirmBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BalanceSelectSourceConfirmViewModel::class.java)
        val btnBack = binding.appbarSignup.btnBack
        val btnSubmit = binding.btnSubmitPolicy

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_by_source_complete) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val data =
                    BalanceTotalRemain(args?.fragmentBalanceAmountSelectSourceConfirm?.balanceAmount!!)
                val action =
                    BalanceSelectSourceConfirmFragmentDirections.actionConfirmToSelectsource(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
            val data =
                BalanceTotalRemain(args?.fragmentBalanceAmountSelectSourceConfirm?.balanceAmount!!)
            val action =
                BalanceSelectSourceConfirmFragmentDirections.actionConfirmToSelectsource(
                    data
                )
            findNavController().navigate(action)
        })

        binding.card = args?.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }

}