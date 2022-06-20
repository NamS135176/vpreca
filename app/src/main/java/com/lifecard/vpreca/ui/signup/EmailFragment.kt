package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {

    private var _binding:FragmentEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel:EmailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        val inputEmail = binding.forgotPassEmailInput
        val btnSubmitEmail = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.action_email_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "入力途中ですがキャンセル\n" +
                            "してもよろしいですか？"
                )
            }.create().show()
        }
        btnBack.setOnClickListener { findNavController().navigate(R.id.action_email_to_phone) }

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_email_to_phone)
            }
        })

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }

        viewModel.emailError.observe(viewLifecycleOwner) { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmitEmail.isEnabled = isValid
        }


        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val action = EmailFragmentDirections.actionEmailCfemail(
                    GiftCardConfirmData(inputEmail.text.toString())
                )
                findNavController().navigate(action)
            }
        }

        inputEmail.doAfterTextChanged { text -> viewModel.emailDataChanged(text = text.toString()) }

        btnSubmitEmail.setOnClickListener {
            viewModel.submit()
        }
        return binding.root
    }

}