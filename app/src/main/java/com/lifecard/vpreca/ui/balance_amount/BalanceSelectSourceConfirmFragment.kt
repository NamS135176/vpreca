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
import com.lifecard.vpreca.databinding.FragmentBalanceSelectSourceConfirmBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
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
    ): View {
        _binding = FragmentBalanceSelectSourceConfirmBinding.inflate(inflater, container, false)
        val btnBack = binding.appbarSignup.btnBack
        val btnSubmit = binding.btnSubmitPolicy

        btnSubmit.setOnClickListener {
            viewModel.creditCardSelectDataChanged(
                args.fragmentBalanceAmountSelectSourceConfirm?.cardSchemeId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.designId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.cardNickname!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.precaNumber!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!
            )
        }

        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_balance_by_source_complete)
                }
                feeInfoResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                feeInfoResult.networkTrouble?.let {
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

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.card = args.fragmentBalanceAmountSelectSourceConfirm
        return binding.root
    }

}