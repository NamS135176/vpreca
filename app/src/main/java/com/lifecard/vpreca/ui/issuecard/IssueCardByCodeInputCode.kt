package com.lifecard.vpreca.ui.issuecard

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
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeInputCodeBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueCardByCodeInputCode : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeInputCode()
    }

    private val viewModel: IssueCardByCodeInputCodeViewModel by viewModels()
    private var _binding: FragmentIssueCardByCodeInputCodeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIssueCardByCodeInputCodeBinding.inflate(inflater, container, false)
        val giftCodeLayout = binding.issueCardByCodeInputLayout
        val giftCodeEdt = binding.issueCardByCodeInputCode
        val btnSubmit = binding.btnSubmitPolicy
        val btnCancel = binding.appbarSignup.cancelBtn
        val buttonOcrDetection = binding.buttonOcrDetection
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_inputcode_to_main)
                }
            })
        btnCancel.setOnClickListener { findNavController().navigate(R.id.action_inputcode_to_main) }
        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { signupFormState ->
            if (giftCodeEdt.text.toString() == "") {
                btnSubmit.isEnabled = false
            } else {
                btnSubmit.isEnabled =
                    signupFormState.giftCodeError == null
            }
        }

        viewModel.giftCodeError.observe(viewLifecycleOwner) { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftCodeDataChanged(text = text.toString()) }

        viewModel.giftInfoResult.observe(
            viewLifecycleOwner,
            Observer { giftInfoResult ->
                giftInfoResult ?: return@Observer
                giftInfoResult.success?.let {
                    val data = BalanceGiftData(
                        "",
                        giftInfoResult.success.giftAmount,
                        giftInfoResult.success.giftNumber
                    )
                    val action =
                        IssueCardByCodeInputCodeDirections.actionInputcodeToConfirmValue(data)
                    findNavController().navigate(action)
                }
                giftInfoResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                giftInfoResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        btnSubmit.setOnClickListener { viewModel.getGiftData(giftCodeEdt.text.toString()) }
        buttonOcrDetection.setOnClickListener {
            val action =
                IssueCardByCodeInputCodeDirections.actionToCameraOcr(getString(R.string.camera_ocr_hint_input_gift_card))
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livedata = getNavigationResult("ocr_code")
        livedata?.observe(viewLifecycleOwner) { ocr ->
            livedata.removeObservers(viewLifecycleOwner)
            if (!ocr.isNullOrEmpty()) {
                val textCode = binding.issueCardByCodeInputCode
                textCode.setText(ocr)
                showToast(getString(R.string.camera_ocr_success), toastPosition = ToastPosition.Top)
                println("GiftCardPolicyFragment... get ocr code $ocr")
            }
            livedata.value = null
        }
    }

}