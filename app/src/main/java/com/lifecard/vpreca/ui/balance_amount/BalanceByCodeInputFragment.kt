package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeInputBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceByCodeInputFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeInputFragment()
    }

    private val viewModel: BalanceByCodeInputViewModel by viewModels()
    private var _binding: FragmentBalanceByCodeInputBinding? = null
    private val binding get() = _binding!!
    private val args: BalanceByCodeInputFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBalanceByCodeInputBinding.inflate(inflater, container, false)
        val totalAmount = binding.tvTotalAmount
        val giftCodeLayout = binding.issueCardByCodeInputLayout
        val giftCodeEdt = binding.issueCardByCodeInputCode
        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack
        val buttonOcrDetection = binding.buttonOcrDetection

        buttonOcrDetection.setOnClickListener {
            val action =
                BalanceByCodeInputFragmentDirections.actionToCameraOcr(getString(R.string.camera_ocr_hint_missing_balance))
            findNavController().navigate(action)
        }

        val fakeBalanceAmount = args.balanceTotalRemain?.balanceAmount?.toInt()!!

        totalAmount.text = Converter.convertCurrency(fakeBalanceAmount)

        btnBack.setOnClickListener { findNavController().navigate(R.id.nav_balance_amount_menu) }
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_balance_amount_menu)
            }
        })
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
                        args.balanceTotalRemain?.balanceAmount!!,
                        giftInfoResult.success.giftAmount,
                        giftInfoResult.success.giftNumber
                    )
                    val action =
                        BalanceByCodeInputFragmentDirections.actionInputToValueConfirm(
                            data
                        )
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
            }
            livedata.value = null
        }
    }

}