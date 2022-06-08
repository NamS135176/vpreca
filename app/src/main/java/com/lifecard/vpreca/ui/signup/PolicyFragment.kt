package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentPolicyBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment


class PolicyFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PolicyViewModel::class.java)
    }

    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
//            OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.nav_login)
//            }
//        })
        val btnSubmitPolicy = binding.btnSubmitPolicy
        val cbPolicy = binding.cbPolicy
        val cancelButton = binding.appbarPolicy.cancelBtn
        val buttonTermOfUse = binding.buttonTermOfUse
        val buttonPolicy = binding.buttonPolicy
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_login)
            }
        })
        cbPolicy.isChecked = false
        btnSubmitPolicy.isEnabled = false
        cancelButton.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    findNavController().popBackStack()
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        cbPolicy.setOnClickListener(View.OnClickListener {
            btnSubmitPolicy.isEnabled = cbPolicy.isChecked
        })

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
//        return inflater.inflate(R.layout.fragment_policy, container, false)

    }
}