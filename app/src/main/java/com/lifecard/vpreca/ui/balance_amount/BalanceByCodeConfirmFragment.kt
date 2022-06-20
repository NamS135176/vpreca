package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
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
    ): View {
//        viewModel = ViewModelProvider(this).get(BalanceByCodeConfirmViewModel::class.java)
        _binding = FragmentBalanceByCodeConfirmBinding.inflate(inflater, container, false)

        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack
        btnBack.setOnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
            val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(
                giftCardConfirmData,
                args.fragmentBalanceAmountSelectSourceConfirm
            )
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val giftCardConfirmData = GiftCardConfirmData("balanceByCodeValueConfirm")
                val action = BalanceByCodeConfirmFragmentDirections.actionConfirmToSelectDesign(
                    giftCardConfirmData,
                    args.fragmentBalanceAmountSelectSourceConfirm
                )
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
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                issueGiftReqResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        btnSubmit.setOnClickListener {
            viewModel.issueGiftCardWithoutCard(
                args.designCard?.designId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.giftNumber!!
            )
        }

        binding.card = args.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }
}