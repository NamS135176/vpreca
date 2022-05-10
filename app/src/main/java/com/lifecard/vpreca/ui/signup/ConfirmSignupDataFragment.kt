package com.lifecard.vpreca.ui.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmSignupDataBinding
import com.lifecard.vpreca.databinding.FragmentPolicyBinding

class ConfirmSignupDataFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmSignupDataFragment()
    }

    private lateinit var viewModel: ConfirmSignupDataViewModel
    private val args: ConfirmSignupDataFragmentArgs by navArgs()
    private var _binding : FragmentConfirmSignupDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmSignupDataBinding.inflate(inflater, container, false)
        val tvId = binding.tvConfirmID
        val tvUsername = binding.tvConfirmUsername
        val btnCancelSubmit = binding.appbarConfirmSignup.cancelBtn
        val btnBack = binding.btnCancelConfirm
        val btnSubmit = binding.btnSubmitConfirm

        tvId.setText(args?.signupData?.id)
        tvUsername.setText(args?.signupData?.username)
        btnCancelSubmit.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_complete)
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmSignupDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}