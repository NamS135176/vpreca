package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeInputCodeBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectWayBinding

class IssueCardByCodeSelectWay : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeSelectWay()
    }

    private lateinit var viewModel: IssueCardByCodeSelectWayViewModel
    private var _binding: FragmentIssueCardByCodeSelectWayBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(IssueCardByCodeSelectWayViewModel::class.java)
        _binding = FragmentIssueCardByCodeSelectWayBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarSignup.btnBack
        val btnCancel = binding.appbarSignup.cancelBtn
        val btnDontUse = binding.btnDontUseCard
        val btnUseCard = binding.btnUseCard

        btnUseCard.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_select_source) })

        btnDontUse.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                 "カードを発行します。\n" +
                         "よろしいですか？"
                )
            }.create().show()
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val giftCardConfirmData = GiftCardConfirmData("valueConfirm")
                val action = IssueCardByCodeSelectWayDirections.actionSelectwayToSelectdesign(
                    giftCardConfirmData
                )
                findNavController().navigate(action)
            }
        })
        btnBack.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("valueConfirm")
            val action = IssueCardByCodeSelectWayDirections.actionSelectwayToSelectdesign(
                giftCardConfirmData
            )
            findNavController().navigate(action)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    findNavController().navigate(R.id.nav_home)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "操作途中ですがキャンセル\n" +
                            "してもよろしいですか？"
                )
            }.create().show()
        })

        return binding.root
    }


}