package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.BackPressFragment
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.ui.web_direct.WebDirectFragmentArgs
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceAmountMenuFragment : BackPressFragment() {

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
        val view = binding.view
        val btnBack = binding.appbarGiftThird.btnBack
        val btnToWeb = binding.buttonToWeb
        val btnBySource = binding.btnBalanceSelectSource
        val btnByCode = binding.btnBalanceByCode
        val tvTotal = binding.tvTotalAmount
        var totalRemain = 0
        val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()

        btnByCode.setOnClickListener {
            val data = BalanceTotalRemain(totalRemain.toString())
            val action =
                BalanceAmountMenuFragmentDirections.actionMenuToInputcode(
                    data
                )
            findNavController().navigate(action)
        }

        btnBySource.setOnClickListener {
            val data = BalanceTotalRemain(totalRemain.toString())
            println(sumUpSrcCardInfo)
            val action =
                BalanceAmountMenuFragmentDirections.actionMenuToSelectsource(
                    data
                )
            findNavController().navigate(action)
        }

        btnToWeb.setOnClickListener {
            findNavController().navigate(
                R.id.nav_web_direct,
                WebDirectFragmentArgs(screenId = WebDirectScreen.SCREEN_BALANCE_AMOUNT).toBundle()
            )
        }

        btnBack.setOnClickListener { findNavController().popBackStack() }

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
                    for (i in 0..suspendDealResult.success.size - 1) {
                        val data = CardInfoRequestContentInfo(
                            suspendDealResult.success[i].cardSchemeId,
                            suspendDealResult.success[i].precaNumber,
                            suspendDealResult.success[i].vcn
                        )
                        sumUpSrcCardInfo.add(data)
                    }
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

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    showLoadingDialog()
                    view.visibility = View.GONE
                }
                else -> {
                    hideLoadingDialog()
                    view.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }


}