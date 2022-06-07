package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceSelectSourceConfirmData
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardSelectDesignFragmentArgs
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceByCodeConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeConfirmFragment()
    }
    private val args: BalanceByCodeConfirmFragmentArgs by navArgs()
    private val viewModel: BalanceByCodeConfirmViewModel by viewModels()
    private var _binding: FragmentBalanceByCodeConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(this).get(BalanceByCodeConfirmViewModel::class.java)
        _binding = FragmentBalanceByCodeConfirmBinding.inflate(inflater, container, false)

        val btnSubmit = binding.btnSubmitPolicy
        val loading = binding.loading
        val btnBack = binding.appbarSignup.btnBack
        btnBack.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
            val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(giftCardConfirmData,
                args.fragmentBalanceAmountSelectSourceConfirm
            )
            findNavController().navigate(action)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
                val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(giftCardConfirmData,args.fragmentBalanceAmountSelectSourceConfirm)
                findNavController().navigate(action)
            }
        })

        viewModel.issueGiftReqResult.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
                    findNavController().navigate(R.id.nav_balance_by_code_complete)
                }
                issueGiftReqResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("",getString(it)) }
                    error.errorMessage?.let { showPopupMessage("",it) }
                }
                issueGiftReqResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener { viewModel.issueGiftCardWithoutCard(args.designCard?.designId!!, args.fragmentBalanceAmountSelectSourceConfirm?.giftNumber!!)})

        binding.card = args.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }
}