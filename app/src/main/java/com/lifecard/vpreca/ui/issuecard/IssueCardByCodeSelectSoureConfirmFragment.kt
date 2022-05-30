package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSoureConfirmBinding

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
        _binding = FragmentIssueCardByCodeSelectSoureConfirmBinding.inflate(inflater, container, false)
        binding.card = args?.fragmentIssueCardByCodeSelectSoureConfirmData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val btnSubmit = binding.btnSubmitInput

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_complete_with_card) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_issue_card_by_code_select_source)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_select_source) })

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

        return binding.root
    }


}