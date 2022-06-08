package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding



class ConfirmPhoneFragment : Fragment() {

    private var _binding:FragmentConfirmPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ConfirmPhoneViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(ConfirmPhoneViewModel::class.java)
        _binding = FragmentConfirmPhoneBinding.inflate(inflater, container, false)
        val inputPhoneConfirm = binding.forgotPassEmailInput
        val btnSubmitPhoneConfirm = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.nav_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_signup_phone) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_signup_phone)
            }
        })

        btnSubmitPhoneConfirm.setOnClickListener(View.OnClickListener {
        })
        return binding.root
    }


}