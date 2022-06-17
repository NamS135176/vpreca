package com.lifecard.vpreca.ui.issuecard

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectWayBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueCardByCodeSelectWay : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeSelectWay()
    }
    private val args: IssueCardByCodeSelectWayArgs by navArgs()
    private val viewModel: IssueCardByCodeSelectWayViewModel by viewModels()
    private var _binding: FragmentIssueCardByCodeSelectWayBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        viewModel = ViewModelProvider(this).get(IssueCardByCodeSelectWayViewModel::class.java)
        _binding = FragmentIssueCardByCodeSelectWayBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarSignup.btnBack
        val btnCancel = binding.appbarSignup.cancelBtn
        val btnDontUse = binding.btnDontUseCard
        val btnUseCard = binding.btnUseCard
        val tvTotalAmount = binding.tvTotalAmount
        tvTotalAmount.text = Converter.convertCurrency(args.issuePlusData?.giftAmount)

        btnUseCard.setOnClickListener {
            val action =
                IssueCardByCodeSelectWayDirections.actionSelectwayToSelectsource(args.issuePlusData)
            findNavController().navigate(action)
        }

        btnDontUse.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
//                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
                    println(args.issuePlusData)
                    viewModel.issueGiftCardWithoutCard(
                        args.issuePlusData?.balanceAmount!!,
                        args.issuePlusData?.giftNumber!!
                    )
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "カードを発行します。\n" +
                            "よろしいですか？"
                )
            }.create().show()
        }

        viewModel.issueGiftReqResult.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
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

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    viewModel.issueGiftCardWithoutCard(
                        args.issuePlusData?.balanceAmount!!,
                        args.issuePlusData?.giftNumber!!
                    )
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "加算処理を中止して\n" +
                            "発行処理をしても\n" +
                            "よろしいですか？"
                )
            }.create().show()
        }

        return binding.root
    }


}