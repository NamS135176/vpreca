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
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceSelectSourceConfirmBinding
import com.lifecard.vpreca.ui.card.CardBottomSheetCustom
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeSelectSoureConfirmFragmentArgs
import com.lifecard.vpreca.ui.listvpreca.ListVprecaViewModel
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceSelectSourceConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceSelectSourceConfirmFragment()
    }

    private var _binding: FragmentBalanceSelectSourceConfirmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BalanceSelectSourceConfirmViewModel by viewModels()
    private val args: BalanceSelectSourceConfirmFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBalanceSelectSourceConfirmBinding.inflate(inflater, container, false)
        val btnBack = binding.appbarSignup.btnBack
        val btnSubmit = binding.btnSubmitPolicy
        val loading = binding.loading
//        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_by_source_complete) })

        btnSubmit.setOnClickListener(View.OnClickListener {
            viewModel.creditCardSelectDataChanged(
                args.fragmentBalanceAmountSelectSourceConfirm?.cardSchemeId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.designId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.cardNickname!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.precaNumber!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!
            )
        })

        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_balance_by_source_complete)
                }
                feeInfoResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("",getString(it)) }
                    error.errorMessage?.let { showPopupMessage("",it) }
                }
                feeInfoResult.networkTrouble?.let {
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

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val data =
                    BalanceTotalRemain(args.fragmentBalanceAmountSelectSourceConfirm?.balanceAmount!!)
                val action =
                    BalanceSelectSourceConfirmFragmentDirections.actionConfirmToSelectsource(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
            val data =
                BalanceTotalRemain(args.fragmentBalanceAmountSelectSourceConfirm?.balanceAmount!!)
            val action =
                BalanceSelectSourceConfirmFragmentDirections.actionConfirmToSelectsource(
                    data
                )
            findNavController().navigate(action)
        })

        binding.card = args.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }

}