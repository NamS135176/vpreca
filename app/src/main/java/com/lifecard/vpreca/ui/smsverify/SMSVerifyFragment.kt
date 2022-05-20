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
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentSmsVerifyBinding
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.ui.termofuse.TermOfUseViewModel

class SMSVerifyFragment : Fragment() {

    companion object {
        fun newInstance() = SMSVerifyFragment()
    }

    private lateinit var viewModel: SMSVerifyViewModel
    private var _binding: FragmentSmsVerifyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmsVerifyBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SMSVerifyViewModel::class.java)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_login)
                }
            })

        val btnCancel = binding.appbarSmsVerify.cancelBtn
        val btnSubmit = binding.btnVerifySms
        val codeInput = binding.idCodeVerify
        val codeLayout = binding.codeInputLayout
        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    findNavController().popBackStack()
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { smsVerifyState ->
                if (codeInput.text.toString() == "") {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled = (smsVerifyState.codeError == null)
                }
            })

        viewModel.codeError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            codeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })



        codeInput.doAfterTextChanged { text -> viewModel.codeDataChanged(text = text.toString()) }
        codeInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            if (codeInput.text.toString() == "1234") {
                findNavController().navigate(R.id.nav_sms_overtimes)
            } else if (codeInput.text.toString() == "2345") {
                findNavController().navigate(R.id.nav_code_expired)
            } else {
                codeLayout.error = "Wrong code"
            }
        })
        return binding.root
    }

}