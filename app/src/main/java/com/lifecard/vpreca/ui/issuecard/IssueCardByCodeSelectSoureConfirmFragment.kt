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
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSoureConfirmBinding
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
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
        binding.card = args.fragmentIssueCardByCodeSelectSoureConfirmData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val btnSubmit = binding.btnSubmitInput
        val loading = binding.loading
        btnSubmit.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.nav_issue_card_by_code_complete_with_card)
            viewModel.issueGiftCardWithCard(
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.designId!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftNumber!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.cardSchemeId!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.name!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.vcnName!!,
            )
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val data = BalanceGiftData(
                    args.fragmentIssueCardByCodeSelectSoureConfirmData?.designId!!,
                    args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftAmount!!,
                    args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftNumber!!
                )
                val action =
                    IssueCardByCodeSelectSoureConfirmFragmentDirections.actionConfirmToSelectsource(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
            val data = BalanceGiftData(
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.designId!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftAmount!!,
                args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftNumber!!
            )
            val action =
                IssueCardByCodeSelectSoureConfirmFragmentDirections.actionConfirmToSelectsource(data)
            findNavController().navigate(action)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    viewModel.issueGiftCardWithoutCard(
                        args.fragmentIssueCardByCodeSelectSoureConfirmData?.designId!!,
                        args.fragmentIssueCardByCodeSelectSoureConfirmData?.giftNumber!!
                    )
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
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        return binding.root
    }


}