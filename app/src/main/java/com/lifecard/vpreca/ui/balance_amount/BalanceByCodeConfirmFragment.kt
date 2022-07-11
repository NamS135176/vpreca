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
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeConfirmBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.eventbus.ReloadSuspendCard
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

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
        val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()

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

        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_balance_by_code_complete)
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

        viewModel.issueGiftReqResult.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
//                    findNavController().navigate(R.id.nav_balance_by_code_complete)
                    viewModel.creditCardSelectDataChanged(
                        it.cardInfo?.cardSchemeId!!,
                        it.cardInfo.designId,
                        it.cardInfo.cardNickname,
                        it.cardInfo.vcn,
                        sumUpSrcCardInfo
                    )
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