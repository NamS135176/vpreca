package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeInputCodeBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByPlusIntroduceBinding

class IssueCardByCodeInputCode : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeInputCode()
    }

    private lateinit var viewModel: IssueCardByCodeInputCodeViewModel
    private var _binding: FragmentIssueCardByCodeInputCodeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(IssueCardByCodeInputCodeViewModel::class.java)
        _binding = FragmentIssueCardByCodeInputCodeBinding.inflate(inflater, container, false)
        val giftCodeLayout = binding.issueCardByCodeInputLayout
        val giftCodeEdt = binding.issueCardByCodeInputCode
        val btnSubmit = binding.btnSubmitPolicy
        val btnCancel = binding.appbarSignup.cancelBtn

        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_main) })
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_issue_card_main)
            }
        })
        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { signupFormState ->
                if (giftCodeEdt.text.toString() == "" ) {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        signupFormState.giftCodeError == null
                }
            })

        viewModel.giftCodeError.observe(viewLifecycleOwner, Observer { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            } })
        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftCodeDataChanged(text = text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_value_confirm) })

        return binding.root
    }


}