package com.lifecard.vpreca.ui.signup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentPolicyBinding
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.utils.Converter


class PolicyFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PolicyViewModel::class.java)
    }

    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!
    private val args: PolicyFragmentArgs by navArgs()
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
                findNavController().navigate(R.id.action_policy_to_login)
            }
        })

//        val data = findNavController().previousBackStackEntry?.destination?.id
//        if (data == R.id.nav_signup_phone) {
//            cbPolicy.isChecked = true
//            btnSubmitPolicy.isEnabled = true
//        }
//        if(data == R.id.nav_webview){
//            cbPolicy.isChecked = Converter.convertStringToBoolean(args.checkData?.preRoute!!)
//            btnSubmitPolicy.isEnabled = Converter.convertStringToBoolean(args.checkData?.preRoute!!)
//        }

//        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
////
////        cbPolicy.isChecked = sharedPref?.getBoolean("checkedPolicy",false)!!
////        btnSubmitPolicy.isEnabled = sharedPref.getBoolean("checkedPolicy",false)
        cancelButton.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    findNavController().navigate(R.id.action_policy_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
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
//            findNavController().navigate(
//                R.id.nav_webview,
//                WebViewFragment.createBundle("https://vpc.lifecard.co.jp/rule/rule03.html")
//            )
            val action = PolicyFragmentDirections.actionSignupInputToSignupConfirm(
                "https://vpc.lifecard.co.jp/rule/rule03.html",
                GiftCardConfirmData(Converter.convertBooleanToString(cbPolicy.isChecked))
            )
            findNavController().navigate(action)
        })
        buttonTermOfUse.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(
//                R.id.nav_webview,
//                WebViewFragment.createBundle("https://vpc.lifecard.co.jp/rule/index.html")
//            )
            val action = PolicyFragmentDirections.actionSignupInputToSignupConfirm(
                "https://vpc.lifecard.co.jp/rule/index.html",
            GiftCardConfirmData(Converter.convertBooleanToString(cbPolicy.isChecked))
            )
            findNavController().navigate(action)
        })
        return binding.root
//        return inflater.inflate(R.layout.fragment_policy, container, false)

    }
}