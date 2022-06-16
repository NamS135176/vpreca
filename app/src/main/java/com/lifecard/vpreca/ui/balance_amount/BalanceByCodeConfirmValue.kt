package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmValueBinding
import com.lifecard.vpreca.utils.Converter

class BalanceByCodeConfirmValue : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeConfirmValue()
    }

    private var _binding: FragmentBalanceByCodeConfirmValueBinding? = null
    private val binding get() = _binding!!

    private val args: BalanceByCodeConfirmValueArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBalanceByCodeConfirmValueBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarSignup.btnBack
        val btnSubmit = binding.btnSubmitPolicy
        val tvTotalAmount = binding.tvTotalAmount
        val tvGiftAmount = binding.tvGiftValue
        tvTotalAmount.text = Converter.convertCurrency(args.balanceGiftData?.balanceAmount!!)
        tvGiftAmount.text = Converter.convertCurrency(args.balanceGiftData?.giftAmount!!)

        btnBack.setOnClickListener {
            val data = BalanceTotalRemain(args.balanceGiftData?.balanceAmount!!)
            val action =
                BalanceByCodeConfirmValueDirections.actionConfirmToInputcode(
                    data
                )
            findNavController().navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val data = BalanceTotalRemain(args.balanceGiftData?.balanceAmount!!)
                val action =
                    BalanceByCodeConfirmValueDirections.actionConfirmToInputcode(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnSubmit.setOnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
            val balanceGiftData = BalanceGiftData(
                args.balanceGiftData?.balanceAmount!!,
                args.balanceGiftData?.giftAmount!!,
                args.balanceGiftData?.giftNumber!!
            )
            val action = BalanceByCodeConfirmValueDirections.actionConfirmToSelectDesign(
                giftCardConfirmData,
                balanceGiftData
            )
            findNavController().navigate(action)
        }

        return binding.root
    }

}