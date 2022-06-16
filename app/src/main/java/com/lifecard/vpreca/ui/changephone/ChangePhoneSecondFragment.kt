package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneSecondBinding

class ChangePhoneSecondFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneSecondFragment()
    }

    private lateinit var viewModel: ChangePhoneSecondViewModel
    private var _binding: FragmentChangePhoneSecondBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChangePhoneSecondViewModel::class.java]
        _binding = FragmentChangePhoneSecondBinding.inflate(inflater, container, false)
        val inputEmail = binding.forgotPassEmailInput
        val btnSubmitEmail = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack

        btnBack.setOnClickListener { findNavController().popBackStack() }

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
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
                findNavController().navigate(R.id.nav_change_phone_input_phone)
            }
        }

        inputEmail.doAfterTextChanged { text -> viewModel.emailDataChanged(text = text.toString()) }

        btnSubmitEmail.setOnClickListener {
            viewModel.submit()
        }
        return binding.root
    }

}