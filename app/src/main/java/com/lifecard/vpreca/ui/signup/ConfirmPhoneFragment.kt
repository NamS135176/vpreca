package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding
import com.lifecard.vpreca.utils.UserConverter


class ConfirmPhoneFragment : Fragment() {

    private var _binding:FragmentConfirmPhoneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConfirmPhoneViewModel by viewModels()
    private val args:ConfirmPhoneFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPhoneBinding.inflate(inflater, container, false)
        val inputPhoneConfirm = binding.forgotPassEmailInput
        val btnSubmitPhoneConfirm = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn
        val phone = binding.tvPhone

        phone.text = UserConverter.formatPhone(args.cardData?.preRoute!!)

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.action_cfPhone_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "入力途中ですがキャンセル\n" +
                            "してもよろしいですか？"
                )
            }.create().show()
        }
        btnBack.setOnClickListener { findNavController().navigate(R.id.action_cfPhone_to_phone) }

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_cfPhone_to_phone)
            }
        })

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }

        viewModel.cfPhoneError.observe(viewLifecycleOwner) { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmitPhoneConfirm.isEnabled = isValid
        }


        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                findNavController().navigate(R.id.nav_signup_email)
            }
        }

        inputPhoneConfirm.doAfterTextChanged { text -> viewModel.cfPhoneDataChanged(text = text.toString()) }

        btnSubmitPhoneConfirm.setOnClickListener {
            viewModel.submit()
        }

        return binding.root
    }
}