package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceSelectSourceConfirmData
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

        val fakeBalanceAmount = 5000
        val fakeGiftValue = 3000
        var fakeRemain = fakeBalanceAmount - fakeGiftValue

        tvTotalAmount.text = Converter.convertCurrency(fakeBalanceAmount)
        tvGiftAmount.text = Converter.convertCurrency(fakeGiftValue)

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_by_code_input) })
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_balance_by_code_input)
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
            val balanceSelectSourceConfirmData = BalanceSelectSourceConfirmData(fakeBalanceAmount.toString(), fakeGiftValue.toString(), fakeRemain.toString())
            val action = BalanceByCodeConfirmValueDirections.actionConfirmToSelectDesign(giftCardConfirmData,balanceSelectSourceConfirmData)
            findNavController().navigate(action)
        })

        return binding.root
    }

}