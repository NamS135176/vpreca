package com.lifecard.vpreca.ui.smsverify

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.DesignCard
import com.lifecard.vpreca.databinding.FragmentSmsVerifyBinding
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardSelectDesignAdapter
import com.lifecard.vpreca.ui.termofuse.TermOfUseViewModel
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SMSVerifyFragment : Fragment() {

    companion object {
        fun newInstance() = SMSVerifyFragment()
    }

    private val viewModel: SMSVerifyViewModel by viewModels()
    private var _binding: FragmentSmsVerifyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmsVerifyBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(SMSVerifyViewModel::class.java)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_sms_to_login)
                }
            })

        val btnCancel = binding.appbarSmsVerify.cancelBtn
        val btnSubmit = binding.btnSubmitPolicy
        val codeInput = binding.idCodeVerify
        val codeLayout = binding.codeInputLayout

        var certType = "certType"
        var operationType = "operationType"
        var certSumFlg = "0"
        var operationSumFlg = "0"
        var extCertDealId = ""
        viewModel.sendSMSRequest("001","0123456789", certType, operationType, certSumFlg, operationSumFlg)
        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    findNavController().navigate(R.id.action_sms_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })

        viewModel.cfPhoneError.observe(viewLifecycleOwner, Observer { error: Int? ->
            codeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })


        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
//                findNavController().navigate(R.id.nav_signup_email)
            }
        })

        viewModel.sendSMSRequestResult.observe(
            viewLifecycleOwner,
            Observer { listDesignResult ->
                listDesignResult ?: return@Observer
                listDesignResult.success?.let {
                    extCertDealId = listDesignResult.success.certResInfo?.extCertDealId!!
                }
                listDesignResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                listDesignResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        codeInput.doAfterTextChanged { text -> viewModel.cfPhoneDataChanged(text = text.toString()) }
        btnSubmit.setOnClickListener(View.OnClickListener {
           viewModel.submit()
        })
        return binding.root
    }

}