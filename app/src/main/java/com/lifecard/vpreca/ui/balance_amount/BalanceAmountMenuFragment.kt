package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardMainBinding
import com.lifecard.vpreca.ui.card.CardBottomSheetCustom
import com.lifecard.vpreca.ui.listvpreca.ListVprecaFragment
import com.lifecard.vpreca.ui.listvpreca.ListVprecaViewModel
import com.lifecard.vpreca.ui.web_direct.WebDirectFragmentArgs
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.WebDirectScreen
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceAmountMenuFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceAmountMenuFragment()
    }

    private var _binding: FragmentBalanceAmountMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BalanceAmountMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBalanceAmountMenuBinding.inflate(inflater, container, false)
        val loading = binding.loading
        val view = binding.view
        val btnBack = binding.appbarGiftThird.btnBack
        val btnToWeb = binding.buttonToWeb
        val btnBySource = binding.btnBalanceSelectSource
        val btnByCode = binding.btnBalanceByCode
        val tvTotal = binding.tvTotalAmount
        var totalRemain = 0
        btnByCode.setOnClickListener(View.OnClickListener {
            val data = BalanceTotalRemain(totalRemain.toString())
            val action =
                BalanceAmountMenuFragmentDirections.actionMenuToInputcode(
                    data
                )
            findNavController().navigate(action)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })
        btnBySource.setOnClickListener(View.OnClickListener {
            val data = BalanceTotalRemain(totalRemain.toString())
            val action =
                BalanceAmountMenuFragmentDirections.actionMenuToSelectsource(
                    data
                )
            findNavController().navigate(action)
        })

        btnToWeb.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_web_direct,
                WebDirectFragmentArgs(screenId = WebDirectScreen.SCREEN_BALANCE_AMOUNT).toBundle()
            )
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_home)
                }
            })

        viewModel.suspendDealResult.observe(
            viewLifecycleOwner,
            Observer { suspendDealResult ->
                suspendDealResult ?: return@Observer
                suspendDealResult.success?.let {
                    println("BalanceAmountViewModel.suspendDealResult.observe success: ${suspendDealResult.success}")
                    val sumBalance: Int = suspendDealResult.success.sumOf {
                        try {
                            if (it.suspendReasonType == "11" && it.adjustEndFlg == "0") {
                                it.unadjustDifferenceAmount.toInt()
                            } else 0
                        } catch (e: Exception) {
                            0
                        }
                    }
                    totalRemain = sumBalance
                    tvTotal.text = Converter.convertCurrency(sumBalance)
                }
                suspendDealResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage(message = getString(it)) }
                    error.errorMessage?.let { showPopupMessage(message = it) }
                }
                suspendDealResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    loading.visibility = View.VISIBLE
                    view.visibility = View.GONE
                }
                else -> {
                    loading.visibility = View.GONE
                    view.visibility = View.VISIBLE
                }
            }
        })

        return binding.root
    }


}