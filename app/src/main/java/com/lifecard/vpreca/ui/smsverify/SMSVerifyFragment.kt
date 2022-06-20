package com.lifecard.vpreca.ui.smsverify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentSmsVerifyBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SMSVerifyFragment : Fragment() {

    companion object {
        fun newInstance() = SMSVerifyFragment()
    }

    private val viewModel: SMSVerifyViewModel by viewModels()
    private var _binding: FragmentSmsVerifyBinding? = null
    private val binding get() = _binding!!
    private val args:SMSVerifyFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val tvPhone = binding.phoneNumber
        var certType = "certType"
        val operationType = "operationType"
        var extCertDealId = ""
        viewModel.sendSMSRequest(args.loginIdData?.loginId!!)
        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    findNavController().navigate(R.id.action_sms_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }

        viewModel.cfPhoneError.observe(viewLifecycleOwner) { error: Int? ->
            codeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }


        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                viewModel.sendSMSConfirm("001",certType, operationType, codeInput.text.toString(), extCertDealId)
            }
        }

        viewModel.sendSMSRequestResult.observe(
            viewLifecycleOwner,
            Observer { listDesignResult ->
                listDesignResult ?: return@Observer
                listDesignResult.success?.let {
                    extCertDealId = listDesignResult.success.extCertDealId!!
                    certType = listDesignResult.success.certType!!
                    tvPhone.text = UserConverter.formatPhone(listDesignResult.success.ivrTelephoneNumber)
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

        viewModel.sendSMSConfirmResult.observe(
            viewLifecycleOwner,
            Observer { listDesignResult ->
                listDesignResult ?: return@Observer
                listDesignResult.success?.let {
                    findNavController().popBackStack(R.id.nav_login, inclusive = false)
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

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                   showLoadingDialog()
                }
                else -> {
                    hideLoadingDialog()
                }
            }
        }

        codeInput.doAfterTextChanged { text -> viewModel.cfPhoneDataChanged(text = text.toString()) }
        btnSubmit.setOnClickListener {
            viewModel.submit()
        }
        return binding.root
    }

}