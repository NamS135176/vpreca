package com.lifecard.vpreca.ui.changephone

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneFirstBinding
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
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangePhoneSecondViewModel::class.java)
        _binding = FragmentChangePhoneSecondBinding.inflate(inflater, container, false)
        val inputEmail = binding.forgotPassEmailInput
        val btnSubmitEmail = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_phone_first) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_change_phone_first)
            }
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })

        viewModel.emailError.observe(viewLifecycleOwner, Observer { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmitEmail.isEnabled = isValid
            })


        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                findNavController().navigate(R.id.nav_change_phone_input_phone)
            }
        })

        inputEmail.doAfterTextChanged { text -> viewModel.emailDataChanged(text = text.toString()) }

        btnSubmitEmail.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })
        return binding.root
    }

}