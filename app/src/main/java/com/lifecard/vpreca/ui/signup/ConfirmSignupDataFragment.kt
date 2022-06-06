package com.lifecard.vpreca.ui.signup

import android.content.Intent
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
import com.lifecard.vpreca.MainActivity
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
    ): View? {
        _binding = FragmentConfirmSignupDataBinding.inflate(inflater, container, false)
        binding.data = args.signupData
        val btnCancelSubmit = binding.appbarConfirmSignup.cancelBtn
        val btnBack = binding.btnCancelConfirm
        val btnSubmit = binding.btnSubmitConfirm
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_signup_input)
                }
            })

        btnCancelSubmit.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    val intent = Intent(requireContext(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        btnBack.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.nav_signup_input)
            findNavController().popBackStack()
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_complete)
        })

        return binding.root
    }
}