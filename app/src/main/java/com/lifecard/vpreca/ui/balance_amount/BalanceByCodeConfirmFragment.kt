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
import com.lifecard.vpreca.data.model.BalanceSelectSourceConfirmData
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardSelectDesignFragmentArgs

class BalanceByCodeConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeConfirmFragment()
    }
    private val args: BalanceByCodeConfirmFragmentArgs by navArgs()
    private lateinit var viewModel: BalanceByCodeConfirmViewModel
    private var _binding: FragmentBalanceByCodeConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BalanceByCodeConfirmViewModel::class.java)
        _binding = FragmentBalanceByCodeConfirmBinding.inflate(inflater, container, false)

        val btnSubmit = binding.btnSubmitPolicy

        val btnBack = binding.appbarSignup.btnBack
        btnBack.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
            val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(giftCardConfirmData,args?.fragmentBalanceAmountSelectSourceConfirm)
            findNavController().navigate(action)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
                val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(giftCardConfirmData,args?.fragmentBalanceAmountSelectSourceConfirm)
                findNavController().navigate(action)
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_by_code_complete) })

        binding.card = args.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}