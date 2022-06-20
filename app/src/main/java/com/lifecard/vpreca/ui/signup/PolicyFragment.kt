package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentPolicyBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment


class PolicyFragment : Fragment() {

    private val viewModel: PolicyViewModel by viewModels()

    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)

        val btnSubmitPolicy = binding.btnSubmitPolicy
        val cbPolicy = binding.cbPolicy
        val cancelButton = binding.appbarPolicy.cancelBtn
        val buttonTermOfUse = binding.buttonTermOfUse
        val buttonPolicy = binding.buttonPolicy
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_login,inclusive = false)
            }
        })

        cancelButton.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().popBackStack(R.id.nav_login,inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        })

        btnSubmitPolicy.isEnabled = false
        cbPolicy.setOnCheckedChangeListener { _, isChecked ->
            run {
                btnSubmitPolicy.isEnabled = isChecked
            }
        }
        btnSubmitPolicy.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_signup_phone)
        })

        buttonPolicy.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_webview,
                WebViewFragment.createBundle("https://vpc.lifecard.co.jp/rule/rule03.html")
            )
        })
        buttonTermOfUse.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_webview,
                WebViewFragment.createBundle("https://vpc.lifecard.co.jp/rule/index.html")
            )
        })
        return binding.root
    }

}