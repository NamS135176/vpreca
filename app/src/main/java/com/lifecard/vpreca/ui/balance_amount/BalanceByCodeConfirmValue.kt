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
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.data.model.BalanceSelectSourceConfirmData
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmValueBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeValueConfirmDirections
import com.lifecard.vpreca.utils.Converter

class BalanceByCodeConfirmValue : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeConfirmValue()
    }

    private var _binding: FragmentBalanceByCodeConfirmValueBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BalanceByCodeConfirmValueViewModel
    private val args: BalanceByCodeConfirmValueArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BalanceByCodeConfirmValueViewModel::class.java)
        _binding = FragmentBalanceByCodeConfirmValueBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarSignup.btnBack
        val btnSubmit = binding.btnSubmitPolicy
        val tvTotalAmount = binding.tvTotalAmount
        val tvGiftAmount = binding.tvGiftValue

//        val fakeBalanceAmount = 5000
//        val fakeGiftValue = 3000
//        var fakeRemain = fakeBalanceAmount - fakeGiftValue

        tvTotalAmount.text = Converter.convertCurrency(args.balanceGiftData?.balanceAmount!!)
        tvGiftAmount.text = Converter.convertCurrency(args.balanceGiftData?.giftAmount!!)

        btnBack.setOnClickListener(View.OnClickListener {
            val data = BalanceTotalRemain(args.balanceGiftData?.balanceAmount!!)
            val action =
                BalanceByCodeConfirmValueDirections.actionConfirmToInputcode(
                    data
                )
            findNavController().navigate(action)
        })
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.nav_balance_by_code_input)
                val data = BalanceTotalRemain(args.balanceGiftData?.balanceAmount!!)
                val action =
                    BalanceByCodeConfirmValueDirections.actionConfirmToInputcode(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
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
        })

        return binding.root
    }

}