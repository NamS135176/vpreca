package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentPolicyBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.ui.webview.WebViewFragmentDirections
import com.lifecard.vpreca.utils.Converter


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
                findNavController().navigate(R.id.action_policy_to_login)
            }
        })

        cancelButton.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    findNavController().navigate(R.id.action_policy_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        })

        cbPolicy.setOnClickListener(View.OnClickListener {
            btnSubmitPolicy.isEnabled = cbPolicy.isChecked
        })
        cbPolicy.setOnCheckedChangeListener { compoundButton, b ->
            run {
                btnSubmitPolicy.isEnabled = b
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
//            val action = PolicyFragmentDirections.actionSignupInputToSignupConfirm(
//                "https://vpc.lifecard.co.jp/rule/rule03.html",
//                GiftCardConfirmData(Converter.convertBooleanToString(cbPolicy.isChecked))
//            )
//            findNavController().navigate(action)
        })
        buttonTermOfUse.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_webview,
                WebViewFragment.createBundle("https://vpc.lifecard.co.jp/rule/index.html")
            )
//            val action = PolicyFragmentDirections.actionSignupInputToSignupConfirm(
//                "https://vpc.lifecard.co.jp/rule/index.html",
//                GiftCardConfirmData(Converter.convertBooleanToString(cbPolicy.isChecked))
//            )
//            findNavController().navigate(action)
        })
        return binding.root
//        return inflater.inflate(R.layout.fragment_policy, container, false)

    }
}