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
import com.lifecard.vpreca.utils.ToastPosition
import com.lifecard.vpreca.utils.getNavigationResult
import com.lifecard.vpreca.utils.showToast

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
        val buttonOcrDetection = binding.buttonOcrDetection

        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_main) })
        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { signupFormState ->
                if (giftCodeEdt.text.toString() == "") {
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
            }
        })
        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftCodeDataChanged(text = text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_value_confirm) })

        buttonOcrDetection.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_camera_ocr) })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livedata = getNavigationResult("ocr_code")
        livedata?.observe(viewLifecycleOwner, Observer { ocr ->
            livedata.removeObservers(viewLifecycleOwner)
            if (!ocr.isNullOrEmpty()) {
                val textCode = binding.issueCardByCodeInputCode
                textCode.setText(ocr)
                showToast(getString(R.string.camera_ocr_success), toastPosition = ToastPosition.Top)
                println("GiftCardPolicyFragment... get ocr code $ocr")
            }
        })
    }

}