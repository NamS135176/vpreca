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
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.databinding.FragmentBalanceSelectSourceConfirmBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.eventbus.ReloadSuspendCard
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

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
        val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()

        btnSubmit.setOnClickListener {
            viewModel.creditCardSelectDataChanged(
                args.fragmentBalanceAmountSelectSourceConfirm?.cardSchemeId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.designId!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.cardNickname!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.precaNumber!!,
                args.fragmentBalanceAmountSelectSourceConfirm?.vcn!!,
                sumUpSrcCardInfo
            )
        }

        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_balance_by_source_complete)
                    EventBus.getDefault().post(ReloadCard())
                    EventBus.getDefault().post(ReloadSuspendCard())
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

        viewModel.suspendDealResult.observe(
            viewLifecycleOwner,
            Observer { suspendDealResult ->
                suspendDealResult ?: return@Observer
                suspendDealResult.success?.let {
                    println("BalanceAmountViewModel.suspendDealResult.observe success: ${suspendDealResult.success}")
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