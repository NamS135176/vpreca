package com.lifecard.vpreca.ui.issuecard

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSoureConfirmBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueCardByCodeSelectSoureConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeSelectSoureConfirmFragment()
    }

    private val args: IssueCardByCodeSelectSoureConfirmFragmentArgs by navArgs()
    private var _binding: FragmentIssueCardByCodeSelectSoureConfirmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IssueCardByCodeSelectSoureConfirmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentIssueCardByCodeSelectSoureConfirmBinding.inflate(inflater, container, false)
//        binding.card = args.fragmentIssueCardByCodeSelectSoureConfirmData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val btnSubmit = binding.btnSubmitInput
        val tvGiftAmount = binding.tvBigGiftValue
        val tvGift = binding.tvGiftValue
        val tvTotal = binding.tvSelectAmount

        var listCardInfo:List<CardInfo> = emptyList()

        tvGiftAmount.text = Converter.convertCurrency(args.designData?.giftAmount)

        var sum = 0
        binding.cardInfo.cardInfo.setBackgroundResource(args.designData.getBackgroundCard())

        args.selectSourceData?.listCard?.forEachIndexed { index, creditCard ->
            run {
                if (args.selectSourceData?.listSelectCard?.get(index)?.isSelected!! == "1") {
                    sum += creditCard.publishAmount?.toInt()!!
                }
            }
        }
        tvGift.text = Converter.convertCurrency(sum)
        tvTotal.text = Converter.convertCurrency((sum + args.designData?.giftAmount?.toInt()!!))

        btnSubmit.setOnClickListener {
            val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()
            args.selectSourceData?.listCard?.forEachIndexed { index, creditCard ->
                if (args.selectSourceData?.listSelectCard?.get(index)?.isSelected == "1"
                ) {
                    val data = CardInfoRequestContentInfo(
                        creditCard.cardSchemeId,
                        creditCard.precaNumber,
                        creditCard.vcn
                    )
                    sumUpSrcCardInfo.add(data)
                }
            }

            viewModel.creditCardSelectDataChanged(
                sumUpSrcCardInfo,
                args.designData?.balanceAmount!!,
                args.designData?.giftNumber!!
            )
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

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("??????") { _, _ ->
                    viewModel.issueGiftCardWithoutCard(
                        args.designData?.balanceAmount!!,
                        args.designData?.giftNumber!!
                    )
                }
                setNegativeButton("?????????", null)
                setMessage(
                    "???????????????????????????\n" +
                            "????????????????????????\n" +
                            "????????????????????????"
                )
            }.create().show()
        }


        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_with_card)
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
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
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

        return binding.root
    }


}