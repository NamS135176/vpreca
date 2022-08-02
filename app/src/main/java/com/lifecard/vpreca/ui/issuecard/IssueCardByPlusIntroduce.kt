package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardByPlusIntroduceBinding

class IssueCardByPlusIntroduce : Fragment() {

    private var _binding: FragmentIssueCardByPlusIntroduceBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIssueCardByPlusIntroduceBinding.inflate(inflater, container, false)
        val btnSubmit = binding.btnSubmitIntroduceFirst
        val btnCancel = binding.appbarGiftThird.cancelBtn
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_issue_introduce_to_main)
            }
        })
        btnCancel.setOnClickListener { findNavController().navigate(R.id.action_issue_introduce_to_main) }
        btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.nav_issue_card_select_source)
        }
        return binding.root
    }

}