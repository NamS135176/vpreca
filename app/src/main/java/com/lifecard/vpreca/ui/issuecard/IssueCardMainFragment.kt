package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardMainBinding
import com.lifecard.vpreca.ui.web_direct.WebDirectFragmentArgs
import com.lifecard.vpreca.utils.WebDirectScreen

class IssueCardMainFragment : Fragment() {
    private var _binding: FragmentIssueCardMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIssueCardMainBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val buttonCreditCard = binding.buttonCreditCard
        val buttonNewCard = binding.buttonNewCard
        val buttonIssueByCode = binding.buttonIssueByCode

        btnCancel.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_home)
        })

        buttonCreditCard.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_web_direct,
                WebDirectFragmentArgs(screenId = WebDirectScreen.SCREEN_ISSUE_CARD_WITH_CREDIT_CARD).toBundle()
            )
        })

        buttonNewCard.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_issue_card_by_plus_introduce)
        })

        buttonIssueByCode.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_input) })

        return binding.root
    }
}