package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeCompleteWithCardBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeCompleteWithoutCardBinding

class IssueCardByCodeCompleteWithCardFragment : Fragment() {
    private var _binding: FragmentIssueCardByCodeCompleteWithCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentIssueCardByCodeCompleteWithCardBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_with_complete_to_home)
            }
        })

        val btnSubmit = binding.btnSubmitPolicy
        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.action_with_complete_to_home) })
        return binding.root
    }


}