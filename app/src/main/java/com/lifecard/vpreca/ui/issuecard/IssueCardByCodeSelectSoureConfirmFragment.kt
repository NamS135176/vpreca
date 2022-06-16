package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
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

    private lateinit var viewModel: IssueCardByCodeSelectSoureConfirmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(IssueCardByCodeSelectSoureConfirmViewModel::class.java)
        _binding =
            FragmentIssueCardByCodeSelectSoureConfirmBinding.inflate(inflater, container, false)
//        binding.card = args.fragmentIssueCardByCodeSelectSoureConfirmData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val btnSubmit = binding.btnSubmitInput
        val loading = binding.loading
        val tvGiftAmount = binding.tvBigGiftValue
        val tvGift = binding.tvGiftValue
        val tvTotal = binding.tvSelectAmount

        var listCardInfo:List<CardInfo> = emptyList()

        tvGiftAmount.text = Converter.convertCurrency(args.designData?.giftAmount)

        viewModel.issueGiftCardWithoutCard(
            args.designData?.balanceAmount!!,
            args.designData?.giftNumber!!
        )
        var sum = 0


        args.selectSourceData?.listCard?.forEachIndexed { index, creditCard ->
            run {
                if (args.selectSourceData?.listSelectCard?.get(index)?.isSelected!! == "1") {
                    sum += creditCard.publishAmount.toInt()
                }
            }
        }
        tvGift.text = Converter.convertCurrency(sum)
        tvTotal.text = Converter.convertCurrency((sum + args.designData?.giftAmount?.toInt()!!))

        btnSubmit.setOnClickListener(View.OnClickListener {
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
                listCardInfo[0].cardSchemeId!!,
                listCardInfo[0].designId,
                listCardInfo[0].cardNickname!!,
                listCardInfo[0].vcnName!!,
                sumUpSrcCardInfo
            )
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action =
                    IssueCardByCodeSelectSoureConfirmFragmentDirections.actionConfirmToSelectsource(
                        args.designData
                    )
                findNavController().popBackStack()
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
//            val data = BalanceGiftData(
//                args.fragmentIssueCardByCodeSelectSoureConfirmData?.designId!!,
//                args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftAmount!!,
//                args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftNumber!!
//            )
            val action =
                IssueCardByCodeSelectSoureConfirmFragmentDirections.actionConfirmToSelectsource(args.designData)
            findNavController().popBackStack()
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "加算処理を中止して\n" +
                            "発行処理をしても\n" +
                            "よろしいですか？"
                )
            }.create().show()
        })

        viewModel.issueGiftReqResult.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
                    binding.card = it.cardInfo
                    binding.cardInfo.card = it.cardInfo
                    listCardInfo = listOf(it.cardInfo!!)
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

        viewModel.issueGiftReqResultWithCard.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_with_card)
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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        })

        return binding.root
    }


}