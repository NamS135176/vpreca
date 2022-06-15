package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmEmailBinding
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding
import com.lifecard.vpreca.utils.UserConverter


class ConfirmEmailFragment : Fragment() {

    private var _binding:FragmentConfirmEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : ConfirmEmailViewModel
    private val args:ConfirmEmailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(ConfirmEmailViewModel::class.java)
        _binding = FragmentConfirmEmailBinding.inflate(inflater, container, false)
        val inputPhoneConfirm = binding.forgotPassEmailInput
        val btnSubmitPhoneConfirm = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn
        val tvEmail = binding.tvEmail

        tvEmail.text = UserConverter.formatPhone(args.mailData?.preRoute!!)
        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.action_cfEmail_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("入力途中ですがキャンセル\n" +
                        "してもよろしいですか？")
            }.create().show()
        })
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.action_cfEmail_to_email) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_cfEmail_to_email)
            }
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })

        viewModel.cfEmailError.observe(viewLifecycleOwner, Observer { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmitPhoneConfirm.isEnabled = isValid
            })


        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                findNavController().navigate(R.id.nav_signup_input)
            }
        })

        inputPhoneConfirm.doAfterTextChanged { text -> viewModel.cfEmailDataChanged(text = text.toString()) }

        btnSubmitPhoneConfirm.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })
        return binding.root
    }

}