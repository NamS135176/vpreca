package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmSignupDataBinding

class ConfirmSignupDataFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmSignupDataFragment()
    }

    private val viewModel: ConfirmSignupDataViewModel by viewModels()
    private val args: ConfirmSignupDataFragmentArgs by navArgs()
    private var _binding: FragmentConfirmSignupDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmSignupDataBinding.inflate(inflater, container, false)
        args.signupData?.let { data -> binding.data = data }
        val btnCancelSubmit = binding.appbarConfirmSignup.cancelBtn
        val btnBack = binding.btnCancelConfirm
        val btnSubmit = binding.btnSubmitConfirm
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        btnCancelSubmit.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    findNavController().popBackStack(R.id.nav_login,inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.nav_complete)
        }

        return binding.root
    }
}