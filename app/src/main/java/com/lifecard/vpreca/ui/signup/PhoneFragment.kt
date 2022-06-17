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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentPhoneBinding


class PhoneFragment : Fragment() {

    private val phoneViewModel: PhoneViewModel by viewModels()
    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        val inputPhone = binding.forgotPassEmailInput
        val btnSubmitPhone = binding.btnSubmitPolicy
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn
        val layout = binding.forgotPassEmailLayout
        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.action_phone_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "入力途中ですがキャンセル\n" +
                            "してもよろしいですか？"
                )
            }.create().show()
        }
        btnBack.setOnClickListener { findNavController().popBackStack() }

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        phoneViewModel.formState.observe(viewLifecycleOwner) { phoneViewModel.checkFormValid() }

        phoneViewModel.phoneError.observe(viewLifecycleOwner) { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        phoneViewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmitPhone.isEnabled = isValid
        }


        phoneViewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val action = PhoneFragmentDirections.actionPhoneToConfirm(
                    GiftCardConfirmData(inputPhone.text.toString())
                )
                findNavController().navigate(action)
//                findNavController().navigate(R.id.nav_signup_confirm_phone)
            }
        }

        inputPhone.doAfterTextChanged { text -> phoneViewModel.phoneDataChanged(text = text.toString()) }

        btnSubmitPhone.setOnClickListener {
            phoneViewModel.submit()
        }
        return binding.root
    }


}