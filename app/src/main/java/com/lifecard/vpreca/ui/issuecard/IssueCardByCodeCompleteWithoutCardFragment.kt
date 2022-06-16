package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeCompleteWithoutCardBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectWayBinding


class IssueCardByCodeCompleteWithoutCardFragment : Fragment() {

    private var _binding: FragmentIssueCardByCodeCompleteWithoutCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentIssueCardByCodeCompleteWithoutCardBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_without_complete_to_home)
            }
        })

        val btnSubmit = binding.btnSubmitPolicy
        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.action_without_complete_to_home) })
        return binding.root
    }


}