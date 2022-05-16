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
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
                findNavController().popBackStack()
            }
        })
        val btnSubmitPolicy = binding.btnSubmitPolicy
        val cbPolicy = binding.cbPolicy
        val rcPolicy = binding.svPolicy
        val cancelButton = binding.appbarPolicy.cancelBtn
        val buttonTermOfUse = binding.buttonTermOfUse
        val buttonPolicy = binding.buttonPolicy

        cbPolicy.isChecked = false
        btnSubmitPolicy.isEnabled = false
        cancelButton.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
//                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
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
            findNavController().navigate(R.id.nav_signup_input)
        })

        buttonPolicy.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_webview,
                WebViewFragment.createBundle("https://www.lifecard.co.jp/privacy_policy/")
            )
        })
        buttonTermOfUse.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.nav_webview,
                WebViewFragment.createBundle("https://www.lifecard.co.jp/efforts/privacy_policy/")
            )
        })


        val arrPolicy: ArrayList<String>
        arrPolicy = ArrayList()
        for (i in 0 until 12) {
            arrPolicy.add("私は、暴力団などの反社会的勢力や、反社会的勢力の関係者ではないことを表明します。")
        }
        Log.e("months", arrPolicy.toString())
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = PolicyAdapter(arrPolicy)
        rcPolicy?.layoutManager = linearLayoutManager
        rcPolicy?.adapter = adapter

        return binding.root
//        return inflater.inflate(R.layout.fragment_policy, container, false)

    }
}